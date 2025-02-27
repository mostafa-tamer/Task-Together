package com.mostafatamer.chatwithme.data.repository.realtime


//class StompRepository(
//    private val user: UserDto,
//    private val stompService: StompService,
//) {
//
//    val service get() = stompService
//
//    fun observeNewFriendshipChat(onNewChat: (Chat) -> Unit) {
//        val topic = WebSocketPaths.AcceptFriendRequestMessageBroker
//            .withUsername(user.username)
//
//        stompService.topicListener(topic, Chat::class.java) { chat ->
//            onNewChat(chat)
//        }
//    }
//
//    fun observeFriendRemovedMe(onRemoved: (Chat) -> Unit) {
//        val topic = WebSocketPaths.RemoveFriendMessageBroker
//            .withUsername(user.username)
//
//        stompService.topicListener(topic, Chat::class.java) { chat ->
//            onRemoved(chat)
//        }
//    }
//
//    fun observeNewGroupChat(onNewChat: (Chat) -> Unit) {
//        val topic = WebSocketPaths.AddedToGroupMessageBroker
//            .withUsername(user.username)
//
//        stompService.topicListener(topic, Chat::class.java) { chat ->
//            onNewChat(chat)
//        }
//    }
//
//    fun observeIfChatReceivedNewMessage(onNewMessage: (MessageDto) -> Unit) {
//        val topic = WebSocketPaths.SendMessageToChatMessageBroker
//            .withUsername(user.username)
//
//        stompService.topicListener(topic, MessageDto::class.java) { messageDto ->
//            onNewMessage(messageDto)
//        }
//    }
//
//    fun observeFriendRequestsAndLoadFriends(
//        onSubscribe: () -> Unit,
//        onNewFriendRequest: (FriendRequestDto) -> Unit,
//    ) {
//        val topic =
//            "${WebSocketPaths.SendFriendRequestMessageBroker.path}/${user.username}"
//
//        stompService.topicListener(
//            topic, FriendRequestDto::class.java, onSubscribe = {
//                onSubscribe()
//
//            }
//        ) { friendRequestDto ->
//            onNewFriendRequest(friendRequestDto)
//        }
//    }
//}