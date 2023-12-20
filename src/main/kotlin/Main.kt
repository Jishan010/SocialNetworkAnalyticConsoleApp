import java.time.Instant

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
                print("Enter user Name: ")
                val userName = readln()
                print("Enter user Age: ")
                val userAge = readlnOrNull()
                println()
                println("UserId : $userId, userName : $userName, userAge : $userAge ")
                socialNetworkManager.addUser(User(id = userId, name = userName, age = userAge?.toIntOrNull() ?: -1))
                println(User(id = userId, name = userName, age = userAge?.toIntOrNull() ?: -1))
            }

            2 -> {
                println()
                print("Enter user Id: ")
                val userId = readln()
                print("Enter user Name: ")
                val userName = readln()
                print("Enter user Age: ")
                val userAge = readlnOrNull()
                println()
                println("UserId : $userId, userName : $userName, userAge : $userAge ")
                val user = User(id = userId, name = userName, age = userAge?.toIntOrNull() ?: -1)
                socialNetworkManager.addUser(user)

                print("Enter id of user you want to friend with: ")
                val userIdToFriendReq = readlnOrNull()
                println()
                socialNetworkManager.addFriend(userIdToFriendReq ?: "", user)
            }

            3 -> {
                println()
                print("Enter user Id: ")
                val userId = readln()

                print("Enter post Id: ")
                val postId = readln()

                val post = Post(
                    postId = postId.toLong(),
                    postContent = "This is my first Post",
                    timestamp = Instant.now(),
                    comments = mutableListOf()
                )
                socialNetworkManager.addPost(userId, post)
            }

            4 -> {
                println()
                print("Enter user your Id: ")
                val userId = readln()

                print("Enter the Id of postOwner: ")
                val postOwnerId = readln()

                print("Enter the Post Id: ")
                val postId = readln()

                val comment = Comment(
                    userId = userId, commentId = 1, comment = "This is my testing comment", timestamp = Instant.now()
                )

                socialNetworkManager.addComment(postOwnerId = postOwnerId, postId = postId.toLong(), comment = comment)
            }

            5 -> {
                print("Enter user Id: ")
                val userId = readln()
                println()
                val posts = socialNetworkManager.getPostsByUser(userId)
                posts?.forEach {
                    println(it.second)
                }
            }

            7 -> {
                println()
                val posts = socialNetworkManager.getPostsCountPerUser()
                for ((userId, postCount) in posts) {
                    println("$userId -> has $postCount number of posts")
                }
            }

            10 -> {
                socialNetworkManager.users.filter {
                    val user = it.value
                    user.friends?.size!! > 0
                }.forEach { (userId, user) ->
                    user.friends?.forEach {
                        println("${user.name} has a friend by the name ${it.name}")
                    }

                }
                println()
                println()
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
        val postUser = users[postOwnerId]
        val post = postUser?.posts?.get(postId)
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

    fun getPostsCountPerUser(): MutableMap<String, Int> {
        val postCountPerUser = mutableMapOf<String, Int>()
        users.values.forEach {
            postCountPerUser[it.id] = it.posts?.size ?: 0
        }
        return postCountPerUser
    }

}

data class User(
    val id: String,
    val name: String,
    val age: Int,
    var friends: MutableList<User>? = mutableListOf(),
    var posts: MutableMap<Long, Post>? = mutableMapOf(),
)

data class Post(val postId: Long, val postContent: String, val timestamp: Instant, val comments: MutableList<Comment>)

data class Comment(val userId: String, val commentId: Long, val comment: String, val timestamp: Instant)
