import java.sql.Timestamp

fun main(args: Array<String>) {
    val socialNetworkManager = SocialNetwork()
    while (true) {
        println("Options:")
        println("1. Add User")
        println("2. Add Friendship")
        println("3. Add Post")
        println("4. Add Comment")
        println("5. Posts by User")
        println("6. Comments Count for Posts")
        println("7. Posts Count per User")
        println("8. Average Comments per Post")
        println("9. Most Active User")
        println("10.Users with Friends and Posts")
        println("11. Exit")
        print("Choose an option: ")
        val userInput = readlnOrNull()

        when (userInput?.toIntOrNull()) {
            1 -> {
                println()
                print("Enter user Id: ")
                val userId = readln()
                println()
                print("Enter user Name: ")
                val userName = readln()
                println()
                print("Enter user Age: ")
                val userAge = readlnOrNull()
                println()
                println("UserId : $userId, userName : $userName, userAge : $userAge ")
                socialNetworkManager.addUser(User(id = userId, name = userName, age = userAge?.toIntOrNull() ?: -1))
                println(User(id = userId, name = userName, age = userAge?.toIntOrNull() ?: -1))
            }

            11 -> break

        }
    }

}


class SocialNetwork() {

    val users: MutableMap<String, User> = mutableMapOf()
    fun addUser(user: User) {
        users[user.id] = user
    }

    fun addFriend(id: String, user: User) {
        val mainUser = users[id]
        mainUser?.friends?.add(user)
    }

    fun addPost(id: String, post: Post) {
        val mainUser = users[id]
        mainUser?.posts?.put(post.postId, post)
    }

    fun addComment(postOwnerId: String, postId: Long, comment: Comment) {
        val postuser = users[postOwnerId]
        val post = postuser?.posts?.get(postId)
        post?.comments?.add(comment)
    }


    fun getPostsByUser(userId: String): List<Pair<Long, Post>>? {
        val user = users[userId]
        val posts = user?.posts
        return posts?.toList()
    }

    fun getCommentsCountsForPosts(userId: String): Int {
        val user = users[userId]
        val posts = user?.posts
        var commentCounts = 0
        posts?.mapValues {
            commentCounts += it.value.comments.size
        }
        return commentCounts
    }

    fun getPostsCountForUser(userId: String): Int? {
        val user = users[userId]
        return user?.posts?.size
    }

    fun getPostsCountPerUser() {
        val postCounts = users.values.map { user: User ->
            user.posts?.size
        }
    }

}

data class User(
    val id: String,
    val name: String,
    val age: Int,
    var friends: MutableList<User>? = mutableListOf(),
    var posts: MutableMap<Long, Post>? = mutableMapOf(),
)

data class Post(val postId: Long, val postContent: String, val timestamp: Timestamp, val comments: MutableList<Comment>)

data class Comment(val commentId: Long, val timestamp: Timestamp)
