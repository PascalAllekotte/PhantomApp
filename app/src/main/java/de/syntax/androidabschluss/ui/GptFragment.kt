package de.syntax.androidabschluss.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import java.util.Locale


class GptFragment : Fragment() {
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var binding: FragmentGptBinding
    private lateinit var edMessage : EditText
   private val chatViewModel : ChatViewModel by lazy {
       ViewModelProvider(this)[ChatViewModel::class.java]
   }

    private val gptArgs : GptFragmentArgs by navArgs()

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

        textToSpeech = TextToSpeech(view.context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    view.context.longToastShow("language is not supported")
                }
            }
        }


        binding.toolbarLayout.backbutton.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.toolbarLayout.backbutton.setOnClickListener{
            findNavController().popBackStack()
        }
        binding.toolbarLayout.titletexttool.text = gptArgs.assistantName

        val chatAdapter = ChatAdapter(){ message, textView ->

            val contextThemeWrapper = ContextThemeWrapper(context, R.style.YourCustomPopupMenuStyle)
            val popup = PopupMenu(contextThemeWrapper, textView)
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            popup.menuInflater.inflate(R.menu.option_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.copyMenu -> {
                        textToSpeech.stop()
                        view.context.copyToClipBoard(message)
                        true
                    }

                    R.id.textToVoiceMenu -> {
                        textToSpeech.speak(
                            message,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )
                        true
                    }


                    R.id.slectTxtMenu -> {
                        textToSpeech.stop()
                        true
                    }
                    R.id.shareTextMenu -> {
                        textToSpeech.stop()
                        view.context.shareMsg(message)
                        true
                    }
                    else -> false
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


        binding.speechButton.setOnClickListener {
            binding.etBlock.text = null
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault()
                )
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something")
                result.launch(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }



                binding.sendButton.setOnClickListener {
                    textToSpeech.stop()
                    view.context.hideKeyBoard(it)
                    if (binding.etBlock.text.toString().trim().isNotEmpty()) {
                        chatViewModel.createChatCompletion(binding.etBlock.text.toString().trim(), gptArgs.assistantId)
                        binding.etBlock.text = null
                    }else{
                        view.context.longToastShow("Message is Required")
                    }
                }

        callGetChatList(binding.chatRv,chatAdapter)
        chatViewModel.getChatList(gptArgs.assistantId)


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

    override fun onDestroyView() {
        super.onDestroyView()
        textToSpeech.stop()
    }
    private val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if (result.resultCode == Activity.RESULT_OK){
            val results = result.data?.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            ) as ArrayList<String>

            binding.etBlock.setText(results[0])
        }
    }
}