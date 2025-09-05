package com.synapse.social.studioasinc.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.adapter.NotificationAdapter
import com.synapse.social.studioasinc.model.Notification

class NotificationsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var notificationList: MutableList<Notification>
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private lateinit var emptyViewStub: ViewStub
    private var emptyView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container)
        recyclerView = view.findViewById(R.id.notifications_list)
        emptyViewStub = view.findViewById(R.id.view_stub_empty)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        notificationList = ArrayList()
        notificationAdapter = NotificationAdapter(requireContext(), notificationList)
        recyclerView.adapter = notificationAdapter

        setupSwipeActions()
        fetchNotifications()

        return view
    }

    private fun fetchNotifications() {
        shimmerFrameLayout.startShimmer()
        shimmerFrameLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null) {
            shimmerFrameLayout.stopShimmer()
            shimmerFrameLayout.visibility = View.GONE
            showEmptyView()
            return
        }

        val reference = FirebaseDatabase.getInstance().getReference("skyline/notifications").child(firebaseUser.uid)
        reference.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                notificationList.clear()
                for (snapshot in dataSnapshot.children) {
                    snapshot.getValue(Notification::class.java)?.let {
                        notificationList.add(it)
                    }
                }

                notificationList.reverse()
                notificationAdapter.notifyDataSetChanged()

                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE

                if (notificationList.isEmpty()) {
                    showEmptyView()
                    recyclerView.visibility = View.GONE
                } else {
                    hideEmptyView()
                    recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
            }
        })
    }

    private fun setupSwipeActions() {
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                markNotificationAsRead(notificationList[position])
            }
        }.attachToRecyclerView(recyclerView)
    }

    private fun markNotificationAsRead(notification: Notification) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null || notification.isRead) {
            return
        }
        val ref = FirebaseDatabase.getInstance().getReference("skyline/notifications")
            .child(firebaseUser.uid)
            .child(notification.timestamp.toString()) // Assuming timestamp is unique key
        ref.child("read").setValue(true)
    }

    private fun markAllNotificationsAsRead() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return
        val ref = FirebaseDatabase.getInstance().getReference("skyline/notifications").child(firebaseUser.uid)
        for (notification in notificationList) {
            if (!notification.isRead) {
                ref.child(notification.timestamp.toString()).child("read").setValue(true)
            }
        }
    }

    private fun showEmptyView() {
        if (emptyView == null) {
            emptyView = emptyViewStub.inflate()
        }
        emptyView?.visibility = View.VISIBLE
    }

    private fun hideEmptyView() {
        emptyView?.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.notifications_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_mark_all_read) {
            markAllNotificationsAsRead()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
