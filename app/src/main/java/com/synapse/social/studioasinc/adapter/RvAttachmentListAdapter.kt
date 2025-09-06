package com.synapse.social.studioasinc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.synapse.social.studioasinc.R
import com.synapse.social.studioasinc.model.AttachmentItem
import java.io.File

class RvAttachmentListAdapter(
    private val attachments: MutableList<AttachmentItem>,
    private val listener: OnAttachmentInteractionListener
) : RecyclerView.Adapter<RvAttachmentListAdapter.ViewHolder>() {

    interface OnAttachmentInteractionListener {
        fun onRemoveAttachment(position: Int, attachment: AttachmentItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_attactment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = attachments[position]
        holder.bind(item, listener)
    }

    override fun getItemCount(): Int = attachments.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val previewIV: ImageView = itemView.findViewById(R.id.previewIV)
        private val closeIV: ImageView = itemView.findViewById(R.id.closeIV)
        private val overlayLL: LinearLayout = itemView.findViewById(R.id.overlayLL)
        private val uploadProgressCPI: CircularProgressIndicator = itemView.findViewById(R.id.uploadProgressCPI)

        fun bind(item: AttachmentItem, listener: OnAttachmentInteractionListener) {
            // Use Glide for background loading
            Glide.with(itemView.context)
                .load(File(item.localPath))
                .placeholder(R.drawable.ph_imgbluredsqure)
                .into(previewIV)

            when (item.uploadState) {
                AttachmentItem.UPLOAD_STATE_UPLOADING -> {
                    overlayLL.visibility = View.VISIBLE
                    uploadProgressCPI.visibility = View.VISIBLE
                    uploadProgressCPI.progress = item.uploadProgress
                    closeIV.visibility = View.GONE
                }
                AttachmentItem.UPLOAD_STATE_SUCCESS -> {
                    overlayLL.visibility = View.GONE
                    uploadProgressCPI.visibility = View.GONE
                    closeIV.visibility = View.VISIBLE
                }
                AttachmentItem.UPLOAD_STATE_FAILED -> {
                    overlayLL.visibility = View.VISIBLE
                    overlayLL.setBackgroundColor(itemView.context.getColor(R.color.upload_failed_overlay))
                    uploadProgressCPI.visibility = View.GONE
                    closeIV.visibility = View.VISIBLE
                }
                else -> { // pending
                    overlayLL.visibility = View.GONE
                    uploadProgressCPI.visibility = View.GONE
                    closeIV.visibility = View.VISIBLE
                }
            }

            closeIV.setOnClickListener {
                listener.onRemoveAttachment(adapterPosition, item)
            }
        }
    }
}
