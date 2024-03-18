package de.syntax.androidabschluss.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.syntax.androidabschluss.R
import de.syntax.androidabschluss.adapter.ChatAdapter
import de.syntax.androidabschluss.databinding.FragmentGptBinding
import de.syntax.androidabschluss.utils.Status
import de.syntax.androidabschluss.utils.copyToClipBoard
import de.syntax.androidabschluss.utils.hideKeyBoard
import de.syntax.androidabschluss.utils.longToastShow
import de.syntax.androidabschluss.utils.shareMsg
import de.syntax.androidabschluss.viewmodel.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class GptFragment : Fragment() {
   private lateinit var binding: FragmentGptBinding
   private val chatViewModel : ChatViewModel by lazy {
       ViewModelProvider(this)[ChatViewModel::class.java]
   }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGptBinding.inflate(inflater,container,false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backbutton.setOnClickListener{
            findNavController().popBackStack()

        }

        val chatAdapter = ChatAdapter(){ message, textView ->

            val popup = PopupMenu(context, textView)
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            popup.menuInflater.inflate(R.menu.option_menu, popup.menu)

            popup.setOnMenuItemClickListener {item ->
                when(item.itemId){
                    R.id.copyMenu -> {
                        view.context.copyToClipBoard(message)
                        return@setOnMenuItemClickListener true
                    }
                    R.id.slectTxtMenu -> {
                        return@setOnMenuItemClickListener true
                    }
                    R.id.shareTextMenu -> {
                        view.context.shareMsg(message)
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener true
                    }
                }
            }
            popup.show()

        }
        binding.chatRv.adapter = chatAdapter
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.chatRv.smoothScrollToPosition(positionStart)
            }
        })

                binding.sendButton.setOnClickListener {
                    view.context.hideKeyBoard(it)
                    if (binding.etBlock.text.toString().trim().isNotEmpty()) {
                        chatViewModel.createChatCompletion(binding.etBlock.text.toString().trim())
                        binding.etBlock.text = null
                    }else{
                        view.context.longToastShow("Message is Required")
                    }
                }

        callGetChatList(binding.chatRv,chatAdapter)
        chatViewModel.getChatList()


    }

    private fun callGetChatList(chatRV: RecyclerView, chatAdapter: ChatAdapter) {
        CoroutineScope(Dispatchers.Main).launch {
            chatViewModel
                .chatStateFlow
                .collectLatest {
                    when (it.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {

                            it.data?.collect { chatlist ->
                                chatAdapter.submitList(chatlist)


                            }
                        }

                        Status.ERROR -> {

                            it.message?.let { it1 -> chatRV.context.longToastShow(it1) }
                        }

                    }
                }

        }

    }


}