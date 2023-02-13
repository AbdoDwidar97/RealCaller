package me.dwidar.realcaller.model.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.dwidar.realcaller.databinding.CallLogItemBinding
import me.dwidar.realcaller.model.components.MyCallLog
import me.dwidar.realcaller.model.interfaces.CallLogActionListener

class CallLogsAdapter(private val myCallLogs : ArrayList<MyCallLog>, private val callLogActionListener: CallLogActionListener) : RecyclerView.Adapter<CallLogsAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        return MyViewHolder(CallLogItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val currentCallLog = myCallLogs[position]
        holder.bindItem(currentCallLog)
        holder.itemBinding.btnGoDetails.setOnClickListener {
            callLogActionListener.onCallLogContactDetailsClick(currentCallLog.contactNumber)
        }

        holder.itemBinding.contactLayout.setOnClickListener {
            callLogActionListener.onCallLogItemClick(currentCallLog.contactNumber)
        }
    }

    override fun getItemCount(): Int {
        return myCallLogs.size
    }

    class MyViewHolder(val itemBinding: CallLogItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root)
    {
        fun bindItem(myCallLog: MyCallLog)
        {
            itemBinding.callerName.text = myCallLog.contactName
            itemBinding.logDate.text = myCallLog.lastCallDate
            itemBinding.logDesc.text = myCallLog.callLogDesc
        }
    }
}