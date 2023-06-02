package com.example.testriq.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testriq.databinding.ItemAppBinding
import com.example.testriq.model.App

class AppAdapter(private val appList: List<App>) :
    RecyclerView.Adapter<AppAdapter.AppViewHolder>() {

    private val selectedItems =  mutableListOf<App>()



    private var binding: ItemAppBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
////        with(holder) {
////            with(appList[position]) {
////                fun bindData(appList: App) {
////                    textName.text = "Name:-  ${makList!!.name}"
////                    textBrand.text = "Brand:-  ${makList!!.brand}"
////                    textDescription.text="Description:-   ${makList!!.description}"
////                    Glide.with(itemView).load(mak   List.image_link)
////                        .apply(RequestOptions().centerCrop())
////                        .into(iv_image)
//        val appList = appList[position]
//        holder.itemView.apply {
//
//                    binding?.ivApp?.setImageDrawable(appList.icon)
//            binding?.tvAppname?.text=appList.name
////                    binding?.cbItem?.isChecked=appList.isSelected
////
////                    binding?.cbItem?.setOnCheckedChangeListener { buttonView, isChecked ->
////                        appList.isSelected = isChecked
////                    }
//
//
////                }
//
////            }
//        }
//        val app = appList[position]
//      with(holder) {
//
//           with(appList[position]) {
//               val packageManager = context.packageManager
//               val appIcon = packageManager.getApplicationIcon(this.packageName)
//               binding.tvAppname.text = this.name
//               binding.ivApp.setImageDrawable(appIcon)
//
//            binding.cbItem.isChecked=app.isSelected
////               Glide.with(binding.root).load(appList.)
////                        .apply(RequestOptions().centerCrop())
////                      .into(binding.ivApp)
//           }
//           }

//        val item = appList[position]
//        holder.binding.cbItem.isChecked = item.isSelected
//        val packageManager = context.packageManager
//        val appIcon = packageManager.getApplicationIcon(item.packageName)

//        holder.binding.tvAppname.text=item.name

        val app = appList[position]
        holder.binding.ivApp.setImageDrawable(app.icon)
        holder.bind(app)

    }

    override fun getItemCount(): Int {
        return appList.size
    }


    inner class AppViewHolder( val binding: ItemAppBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(app: App) {
            binding.apply {
//                val item = appList[position]
//                        val packageManager = context.packageManager
//        val appIcon = packageManager.getApplicationIcon(item.packageName!!)
                cbItem.isChecked = app.isSelected
               cbItem.text = app.name
                ivApp.setImageDrawable(ivApp.drawable)
                cbItem.setOnCheckedChangeListener { _, isChecked ->
                    app.isSelected = isChecked
                }
            }
    }

    // Delete selected items
//    fun deleteSelectedItems() {
//        appList.removeAll(selectedItems)
//        selectedItems.clear()
//        notifyDataSetChanged()
//    }
}}