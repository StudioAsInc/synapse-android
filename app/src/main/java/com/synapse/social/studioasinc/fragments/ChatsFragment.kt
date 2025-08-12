package com.synapse.social.studioasinc.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.synapse.social.studioasinc.ChatActivity
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.SketchwareUtil
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ChatsFragment : Fragment() {

    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var InboxRecyclerView: RecyclerView
    private lateinit var noInbox: TextView

    private val _firebase = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val main = _firebase.getReference("skyline")

    private var ChatInboxList: ArrayList<HashMap<String, Any>> = ArrayList()
    private val UserInfoCacheMap: HashMap<String, Any> = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)
        initialize(view)
        initializeLogic()
        return view
    }

    private fun initialize(view: View) {
        swipeLayout = view.findViewById(R.id.swipeLayout)
        InboxRecyclerView = view.findViewById(R.id.InboxRecyclerView)
        noInbox = view.findViewById(R.id.noInbox)

        swipeLayout.setOnRefreshListener { _getInboxReference() }
    }

    private fun initializeLogic() {
        InboxRecyclerView.adapter = InboxRecyclerViewAdapter(ChatInboxList)
        InboxRecyclerView.layoutManager = LinearLayoutManager(context)
        _getInboxReference()
    }

    override fun onResume() {
        super.onResume()
        _getInboxReference()
    }

    fun _getInboxReference() {
        val getInboxRef: Query =
            FirebaseDatabase.getInstance().getReference("skyline/inbox").child(FirebaseAuth.getInstance().currentUser!!.uid)
        getInboxRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    InboxRecyclerView.visibility = View.VISIBLE
                    noInbox.visibility = View.GONE
                    ChatInboxList.clear()
                    try {
                        val _ind = object : GenericTypeIndicator<HashMap<String?, Any?>?>() {}
                        for (_data in dataSnapshot.children) {
                            val _map = _data.getValue(_ind)
                            ChatInboxList.add(_map as HashMap<String, Any>)
                        }
                    } catch (_e: Exception) {
                        _e.printStackTrace()
                    }
                    SketchwareUtil.sortListMap(ChatInboxList, "push_date", false, false)
                    InboxRecyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    InboxRecyclerView.visibility = View.GONE
                    noInbox.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun _setTime(_currentTime: Double, _txt: TextView) {
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        val time_diff = c1.timeInMillis - _currentTime
        if (time_diff < 60000) {
            if (time_diff / 1000 < 2) {
                _txt.text = "1" + " " + resources.getString(R.string.seconds_ago)
            } else {
                _txt.text = (time_diff / 1000).toLong().toString() + " " + resources.getString(R.string.seconds_ago)
            }
        } else {
            if (time_diff < 60 * 60000) {
                if (time_diff / 60000 < 2) {
                    _txt.text = "1" + " " + resources.getString(R.string.minutes_ago)
                } else {
                    _txt.text = (time_diff / 60000).toLong().toString() + " " + resources.getString(R.string.minutes_ago)
                }
            } else {
                if (time_diff < 24 * (60 * 60000)) {
                    if (time_diff / (60 * 60000) < 2) {
                        _txt.text =
                            (time_diff / (60 * 60000)).toLong().toString() + " " + resources.getString(R.string.hours_ago)
                    } else {
                        _txt.text =
                            (time_diff / (60 * 60000)).toLong().toString() + " " + resources.getString(R.string.hours_ago)
                    }
                } else {
                    if (time_diff < 7 * (24 * (60 * 60000))) {
                        if (time_diff / (24 * (60 * 60000)) < 2) {
                            _txt.text = (time_diff / (24 * (60 * 60000))).toLong()
                                .toString() + " " + resources.getString(R.string.days_ago)
                        } else {
                            _txt.text = (time_diff / (24 * (60 * 60000))).toLong()
                                .toString() + " " + resources.getString(R.string.days_ago)
                        }
                    } else {
                        c2.timeInMillis = _currentTime.toLong()
                        _txt.text = SimpleDateFormat("dd-MM-yyyy").format(c2.time)
                    }
                }
            }
        }
    }

    inner class InboxRecyclerViewAdapter(private val _data: ArrayList<HashMap<String, Any>>) :
        RecyclerView.Adapter<InboxRecyclerViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val _inflater = LayoutInflater.from(context)
            val _v = _inflater.inflate(R.layout.inbox_msg_list_cv_synapse, null)
            val _lp = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            _v.layoutParams = _lp
            return ViewHolder(_v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val view = holder.itemView
            val main = view.findViewById<LinearLayout>(R.id.main)
            val profileCardImage = view.findViewById<ImageView>(R.id.profileCardImage)
            val username = view.findViewById<TextView>(R.id.username)
            val last_message = view.findViewById<TextView>(R.id.last_message)
            val push = view.findViewById<TextView>(R.id.push)
            val message_state = view.findViewById<ImageView>(R.id.message_state)
            val unread_messages_count_badge = view.findViewById<TextView>(R.id.unread_messages_count_badge)
            val userStatusCircleBG = view.findViewById<LinearLayout>(R.id.userStatusCircleBG)
            val genderBadge = view.findViewById<ImageView>(R.id.genderBadge)
            val verifiedBadge = view.findViewById<ImageView>(R.id.verifiedBadge)

            try {
                val _lp = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                view.layoutParams = _lp
                main.visibility = View.GONE
                if (_data[position]["last_message_text"].toString() == "null") {
                    last_message.text = resources.getString(R.string.m_no_chats)
                } else {
                    last_message.text = _data[position]["last_message_text"].toString()
                }
                if (_data[position]["last_message_uid"].toString() == auth.currentUser!!.uid) {
                    if (_data[position]["last_message_state"].toString() == "sended") {
                        message_state.setImageResource(R.drawable.icon_done_round)
                    } else {
                        message_state.setImageResource(R.drawable.icon_done_all_round)
                    }
                    last_message.setTextColor(-0x9f9f9f)
                    push.setTextColor(-0x9f9f9f)
                    message_state.visibility = View.VISIBLE
                    unread_messages_count_badge.visibility = View.GONE
                } else {
                    message_state.visibility = View.GONE
                    val mExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
                    val mMainHandler = Handler(Looper.getMainLooper())
                    mExecutorService.execute {
                        val getUnreadMessagesCount: Query = FirebaseDatabase.getInstance()
                            .getReference("skyline/chats").child(auth.currentUser!!.uid)
                            .child(_data[position]["uid"].toString()).orderByChild("message_state")
                            .equalTo("sended")
                        getUnreadMessagesCount.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                mMainHandler.post {
                                    val unReadMessageCount = dataSnapshot.childrenCount
                                    if (dataSnapshot.exists()) {
                                        last_message.setTextColor(-0x1000000)
                                        push.setTextColor(-0x1000000)
                                        unread_messages_count_badge.text =
                                            unReadMessageCount.toString()
                                        unread_messages_count_badge.visibility = View.VISIBLE
                                    } else {
                                        last_message.setTextColor(-0x9f9f9f)
                                        push.setTextColor(-0x9f9f9f)
                                        unread_messages_count_badge.visibility = View.GONE
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    }
                }
                _setTime(
                    _data[position]["push_date"].toString().toDouble(),
                    push
                )
                if (UserInfoCacheMap.containsKey("uid-" + _data[position]["uid"].toString())) {
                    main.visibility = View.VISIBLE
                    val uid = _data[position]["uid"].toString()
                    val bannedObj = UserInfoCacheMap["banned-$uid"]
                    if (bannedObj != null && bannedObj.toString() == "true") {
                        profileCardImage.setImageResource(R.drawable.banned_avatar)
                    } else {
                        val avatarObj = UserInfoCacheMap["avatar-$uid"]
                        if (avatarObj == null || avatarObj.toString() == "null") {
                            profileCardImage.setImageResource(R.drawable.avatar)
                        } else {
                            Glide.with(requireContext()).load(Uri.parse(avatarObj.toString()))
                                .into(profileCardImage)
                        }
                    }
                    val nicknameObj = UserInfoCacheMap["nickname-$uid"]
                    if (nicknameObj == null || nicknameObj.toString() == "null") {
                        val usernameObj = UserInfoCacheMap["username-$uid"]
                        username.text = "@" + if (usernameObj != null) usernameObj.toString() else "unknown"
                    } else {
                        username.text = nicknameObj.toString()
                    }
                    val statusObj = UserInfoCacheMap["status-$uid"]
                    userStatusCircleBG.visibility =
                        if (statusObj != null && statusObj.toString() == "online") View.VISIBLE else View.GONE
                    val genderObj = UserInfoCacheMap["gender-$uid"]
                    if (genderObj == null || genderObj.toString() == "hidden") {
                        genderBadge.visibility = View.GONE
                    } else {
                        genderBadge.visibility = View.VISIBLE
                        val gender = genderObj.toString()
                        if (gender == "male") {
                            genderBadge.setImageResource(R.drawable.male_badge)
                        } else if (gender == "female") {
                            genderBadge.setImageResource(R.drawable.female_badge)
                        }
                    }
                    val accountTypeObj = UserInfoCacheMap["account_type-$uid"]
                    val premiumObj = UserInfoCacheMap["account_premium-$uid"]
                    val verifyObj = UserInfoCacheMap["verify-$uid"]
                    if (accountTypeObj != null) {
                        val accountType = accountTypeObj.toString()
                        if (accountType == "admin") {
                            verifiedBadge.setImageResource(R.drawable.admin_badge)
                            verifiedBadge.visibility = View.VISIBLE
                        } else if (accountType == "moderator") {
                            verifiedBadge.setImageResource(R.drawable.moderator_badge)
                            verifiedBadge.visibility = View.VISIBLE
                        } else if (accountType == "support") {
                            verifiedBadge.setImageResource(R.drawable.support_badge)
                            verifiedBadge.visibility = View.VISIBLE
                        } else if (premiumObj != null && premiumObj.toString() == "true") {
                            verifiedBadge.setImageResource(R.drawable.premium_badge)
                            verifiedBadge.visibility = View.VISIBLE
                        } else if (verifyObj != null && verifyObj.toString() == "true") {
                            verifiedBadge.setImageResource(R.drawable.verified_badge)
                            verifiedBadge.visibility = View.VISIBLE
                        } else {
                            verifiedBadge.visibility = View.GONE
                        }
                    } else if (premiumObj != null && premiumObj.toString() == "true") {
                        verifiedBadge.setImageResource(R.drawable.premium_badge)
                        verifiedBadge.visibility = View.VISIBLE
                    } else if (verifyObj != null && verifyObj.toString() == "true") {
                        verifiedBadge.setImageResource(R.drawable.verified_badge)
                        verifiedBadge.visibility = View.VISIBLE
                    } else {
                        verifiedBadge.visibility = View.GONE
                    }
                } else {
                    val mExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
                    val mMainHandler = Handler(Looper.getMainLooper())
                    mExecutorService.execute {
                        val getUserReference: DatabaseReference = FirebaseDatabase.getInstance()
                            .getReference("skyline/users").child(_data[position]["uid"].toString())
                        getUserReference.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                mMainHandler.post {
                                    if (dataSnapshot.exists()) {
                                        UserInfoCacheMap["uid-" + _data[position]["uid"].toString()] =
                                            _data[position]["uid"].toString()
                                        UserInfoCacheMap["avatar-" + _data[position]["uid"].toString()] =
                                            dataSnapshot.child("avatar").getValue(
                                                String::class.java
                                            )!!
                                        UserInfoCacheMap["banned-" + _data[position]["uid"].toString()] =
                                            dataSnapshot.child("banned").getValue(
                                                String::class.java
                                            )!!
                                        UserInfoCacheMap["username-" + _data[position]["uid"].toString()] =
                                            dataSnapshot.child("username").getValue(
                                                String::class.java
                                            )!!
                                        UserInfoCacheMap["nickname-" + _data[position]["uid"].toString()] =
                                            dataSnapshot.child("nickname").getValue(
                                                String::class.java
                                            )!!
                                        UserInfoCacheMap["status-" + _data[position]["uid"].toString()] =
                                            dataSnapshot.child("status").getValue(
                                                String::class.java
                                            )!!
                                        UserInfoCacheMap["gender-" + _data[position]["uid"].toString()] =
                                            dataSnapshot.child("gender").getValue(
                                                String::class.java
                                            )!!
                                        UserInfoCacheMap["account_type-" + _data[position]["uid"].toString()] =
                                            dataSnapshot.child("account_type").getValue(
                                                String::class.java
                                            )!!
                                        UserInfoCacheMap["account_premium-" + _data[position]["uid"].toString()] =
                                            dataSnapshot.child("account_premium").getValue(
                                                String::class.java
                                            )!!
                                        UserInfoCacheMap["verify-" + _data[position]["uid"].toString()] =
                                            dataSnapshot.child("verify").getValue(
                                                String::class.java
                                            )!!
                                        main.visibility = View.VISIBLE
                                        if (dataSnapshot.child("banned").getValue(String::class.java) == "true") {
                                            profileCardImage.setImageResource(R.drawable.banned_avatar)
                                        } else {
                                            if (dataSnapshot.child("avatar").getValue(String::class.java) == "null") {
                                                profileCardImage.setImageResource(R.drawable.avatar)
                                            } else {
                                                Glide.with(requireContext()).load(
                                                    Uri.parse(
                                                        dataSnapshot.child("avatar").getValue(String::class.java)
                                                    )
                                                ).into(profileCardImage)
                                            }
                                        }
                                        if (dataSnapshot.child("nickname").getValue(String::class.java) == "null") {
                                            username.text =
                                                "@" + dataSnapshot.child("username").getValue(String::class.java)
                                        } else {
                                            username.text =
                                                dataSnapshot.child("nickname").getValue(String::class.java)
                                        }
                                        if (dataSnapshot.child("status").getValue(String::class.java) == "online") {
                                            userStatusCircleBG.visibility = View.VISIBLE
                                        } else {
                                            userStatusCircleBG.visibility = View.GONE
                                        }
                                        if (dataSnapshot.child("gender").getValue(String::class.java) == "hidden") {
                                            genderBadge.visibility = View.GONE
                                        } else {
                                            if (dataSnapshot.child("gender").getValue(String::class.java) == "male") {
                                                genderBadge.setImageResource(R.drawable.male_badge)
                                                genderBadge.visibility = View.VISIBLE
                                            } else {
                                                if (dataSnapshot.child("gender").getValue(String::class.java) == "female") {
                                                    genderBadge.setImageResource(R.drawable.female_badge)
                                                    genderBadge.visibility = View.VISIBLE
                                                }
                                            }
                                        }
                                        if (dataSnapshot.child("account_type").getValue(String::class.java) == "admin") {
                                            verifiedBadge.setImageResource(R.drawable.admin_badge)
                                            verifiedBadge.visibility = View.VISIBLE
                                        } else {
                                            if (dataSnapshot.child("account_type").getValue(String::class.java) == "moderator") {
                                                verifiedBadge.setImageResource(R.drawable.moderator_badge)
                                                verifiedBadge.visibility = View.VISIBLE
                                            } else {
                                                if (dataSnapshot.child("account_type").getValue(String::class.java) == "support") {
                                                    verifiedBadge.setImageResource(R.drawable.support_badge)
                                                    verifiedBadge.visibility = View.VISIBLE
                                                } else {
                                                    if (dataSnapshot.child("account_premium").getValue(String::class.java) == "true") {
                                                        verifiedBadge.setImageResource(R.drawable.premium_badge)
                                                        verifiedBadge.visibility = View.VISIBLE
                                                    } else {
                                                        if (dataSnapshot.child("verify").getValue(String::class.java) == "true") {
                                                            verifiedBadge.setImageResource(R.drawable.verified_badge)
                                                            verifiedBadge.visibility = View.VISIBLE
                                                        } else {
                                                            verifiedBadge.visibility = View.GONE
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {}
                        })
                    }
                }
                main.setOnClickListener {
                    val intent = Intent(context, ChatActivity::class.java)
                    intent.putExtra("uid", _data[position]["uid"].toString())
                    intent.putExtra("origin", "MessagesActivity")
                    startActivity(intent)
                }
            } catch (e: Exception) {
            }
        }

        override fun getItemCount(): Int {
            return _data.size
        }

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v)
    }
}
