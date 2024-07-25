package com.mostafatamer.tasktogether.presentation.group_screen.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.mostafatamer.tasktogether.HttpStatusCode
import com.mostafatamer.tasktogether.PaginationState
import com.mostafatamer.tasktogether.SocketManager
import com.mostafatamer.tasktogether.data.repositories.GroupsRepository
import com.mostafatamer.tasktogether.domain.model.Message
import com.mostafatamer.tasktogether.domain.model.SendMessage
import com.mostafatamer.tasktogether.getSystemLanguage
import com.mostafatamer.tasktogether.lib.StompLifecycleManager
import com.mostafatamer.tasktogether.match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val socketManager: SocketManager,
    private val groupsRepository: GroupsRepository,
) : GroupViewModel() {
    var isThereNoMessages by mutableStateOf(false)

    val paginationState = PaginationState(25)

    val messages = mutableStateListOf<Message>()
    private val messagesSet = mutableSetOf<Message>()
    private val messageState = mutableSetOf<String>()
    val stompLifecycleManager = StompLifecycleManager(socketManager)

    fun loadMessages() {
        println(paginationState.currentPage)
        println(paginationState.pageSize)
        groupsRepository.getGroupChat(
            group.id!!,
            paginationState.currentPage,
            paginationState.pageSize
        ).setOnResponse { response, code ->
            if (HttpStatusCode.OK.match(code)) {
                val messageResponse = response.data.messages

                messageResponse.forEach {
                    if (!messagesSet.contains(it)) {
                        messages.addSortedDesc(it) { other ->
                            other.timestamp.compareTo(it.timestamp)
                        }
                        messagesSet.add(it)
                    }
                }

                paginationState.hasMorePages = !response.data.isLast
                notifyIfEmpty()
            }
        }.setLoadingObserver {
            paginationState.isLoading = it
        }.execute()
    }


    private fun <T> MutableList<T>.addSortedDesc(item: T, comparator: (T) -> Int) {
        val index = binarySearch {
            val result = comparator(it)
            if (result < 0) 1 else -1
        }
        val insertIndex = if (index < 0) -(index + 1) else index
        add(insertIndex, item)
    }


    fun reset() {
        paginationState.currentPage = 1
        paginationState.hasMorePages = true
        paginationState.isLoading = false
        messages.clear()
        messagesSet.clear()
    }

    fun observeMessages(onReceive: suspend CoroutineScope.(Message) -> Unit) {
        val topic = group.id!!

        socketManager.listen(topic) { messageJson ->
            val message = Gson().fromJson(messageJson.toString(), Message::class.java)

            if (!messagesSet.contains(message)) {
                messages.addSortedDesc(message) { other -> other.timestamp.compareTo(message.timestamp) }
                messagesSet.add(message)
                viewModelScope.launch {
                    onReceive(message)
                }
                notifyIfEmpty()
            }
        }
    }

    fun sendMessage(message: String) {
        val messageJson = Gson().toJson(
            SendMessage(
                content = message,
                groupID = group.id!!,
            )
        )
        socketManager.sendMessage("message", messageJson)
    }

    private fun notifyIfEmpty() {
        isThereNoMessages = messages.isEmpty()
    }

    fun translationRollback(messageDto: Message, index: Int) {
        val oldMessage = messagesSet.find { it.id == messageDto.id }
        messages[index] = messages[index].copy(content = oldMessage!!.content)
        messageState.remove(messageDto.id)
    }

    fun translateMessage(messageDto: Message, index: Int, onTranslate: (Boolean) -> Unit) {
        groupsRepository.translateMessage(messageDto.content, getSystemLanguage())
            .setOnResponse { response, code ->
                println(response)
                if (HttpStatusCode.OK.match(code)) {
                    messages[index] = messages[index].copy(content = response.data)
                    messageState.add(messageDto.id)
                    onTranslate(true)
                } else {
                    onTranslate(false)
                }

                println(response)
            }.setOnFail {
                println(it.message)
            }.execute()

    }

    fun alreadyTranslated(messageDto: Message): Boolean {
        return messageState.contains(messageDto.id)
    }
}
