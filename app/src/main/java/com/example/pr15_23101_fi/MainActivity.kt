package com.example.pr15_23101_fi

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val PREFS_NAME = "MeditationAppPrefs"
        private const val KEY_NICKNAME = "user_nickname"
        private const val KEY_AVATAR_URL = "user_avatar_url"
        private const val KEY_TOKEN = "auth_token"
        private const val BASE_URL = "https://mskko2021.mad.hakta.pro/api/"
    }

    private lateinit var tvWelcomeMessage: TextView
    private lateinit var ivProfileAvatar: ImageView
    private lateinit var rvFeelings: RecyclerView
    private lateinit var ivNavHome: ImageView
    private lateinit var ivNavFeelings: ImageView
    private lateinit var ivNavProfile: ImageView
    private lateinit var prefs: SharedPreferences
    private lateinit var feelingAdapter: FeelingAdapter
    private lateinit var feelingList: MutableList<FeelingItem>
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: Main Activity started")

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        if (!prefs.getBoolean("is_logged_in", false)) {
            Log.w(TAG, "User not logged in, redirecting to Login")
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)

        tvWelcomeMessage = findViewById(R.id.tv_welcome_message)
        ivProfileAvatar = findViewById(R.id.iv_profile_avatar)
        rvFeelings = findViewById(R.id.rv_feelings)
        ivNavHome = findViewById(R.id.iv_nav_home)
        ivNavFeelings = findViewById(R.id.iv_nav_feelings)
        ivNavProfile = findViewById(R.id.iv_nav_profile)

        loadUserData()

        feelingList = mutableListOf()
        feelingAdapter = FeelingAdapter(feelingList, this)
        rvFeelings.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvFeelings.adapter = feelingAdapter

        loadFeelingsFromServer()

        feelingAdapter.setOnItemClickListener { feelingItem, position ->
            Toast.makeText(this, "${getString(R.string.feeling_selected)} ${feelingItem.title}", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Feeling selected: ${feelingItem.title} at position $position")
        }

        ivNavHome.setOnClickListener {
            Log.d(TAG, "Home tab clicked")
        }

        ivNavFeelings.setOnClickListener {
            Log.d(TAG, "Feelings tab clicked")
            // Переход на экран Прослушивания
            val intent = Intent(this@MainActivity, ListenActivity::class.java)
            startActivity(intent)
        }

        ivNavProfile.setOnClickListener {
            Log.d(TAG, "Profile tab clicked")
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageView>(R.id.iv_menu)?.setOnClickListener {
            Log.d(TAG, "Menu button clicked")
            val intent = Intent(this@MainActivity, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadUserData() {
        val nickname = prefs.getString(KEY_NICKNAME, getString(R.string.default_user)) ?: getString(R.string.default_user)
        val avatarUrl = prefs.getString(KEY_AVATAR_URL, "") ?: ""

        tvWelcomeMessage.text = "${getString(R.string.welcome_back)} $nickname!"
        Log.d(TAG, "User data loaded: $nickname")

        if (avatarUrl.isNotEmpty()) {
            Glide.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .circleCrop()
                .into(ivProfileAvatar)
            Log.d(TAG, "Avatar loaded from URL: $avatarUrl")
        } else {
            ivProfileAvatar.setImageResource(R.drawable.default_avatar)
        }
    }

    private fun loadFeelingsFromServer() {
        Log.d(TAG, "Loading feelings from server")

        apiService.getFeelings().enqueue(object : Callback<ApiResponse<FeelingItem>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ApiResponse<FeelingItem>>,
                response: Response<ApiResponse<FeelingItem>>
            ) {
                if (response.isSuccessful && response.body() != null && response.body()!!.success) {
                    val feelings = response.body()!!.data.toMutableList()
                    Log.d(TAG, "Feelings loaded: ${feelings.size} items")

                    Collections.sort(feelings) { f1, f2 ->
                        Integer.compare(f1.position, f2.position)
                    }

                    for (feeling in feelings) {
                        feeling.iconResId = when {
                            feeling.title.contains("Спокой") -> R.drawable.ic_calm
                            feeling.title.contains("Расслаб") -> R.drawable.ic_relaxed
                            feeling.title.contains("Сосредоточ") -> R.drawable.ic_focused
                            feeling.title.contains("Вдохнов") -> R.drawable.ic_inspired
                            else -> R.drawable.ic_calm
                        }
                    }

                    feelingList.clear()
                    feelingList.addAll(feelings)
                    feelingAdapter.notifyDataSetChanged()
                    Log.d(TAG, "Feelings displayed with icons")
                } else {
                    Log.w(TAG, "API returned error: ${response.code()}")
                    loadTestFeelings()
                }
            }

            override fun onFailure(call: Call<ApiResponse<FeelingItem>>, t: Throwable) {
                Log.e(TAG, "Network error: ${t.message}")
                Toast.makeText(this@MainActivity, "${getString(R.string.error_network)} ${t.message}", Toast.LENGTH_SHORT).show()
                loadTestFeelings()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTestFeelings() {
        Log.d(TAG, "Loading test feelings (fallback)")
        feelingList.clear()
        feelingList.add(FeelingItem(getString(R.string.calm), R.drawable.ic_calm))
        feelingList.add(FeelingItem(getString(R.string.relaxed), R.drawable.ic_relaxed))
        feelingList.add(FeelingItem(getString(R.string.focused), R.drawable.ic_focused))
        feelingList.add(FeelingItem(getString(R.string.inspired), R.drawable.ic_inspired))
        feelingAdapter.notifyDataSetChanged()
    }
    fun onFailure(call: Call<ApiResponse<FeelingItem>>, t: Throwable) {
        Log.e(TAG, "Network error: ${t.message}")

        val errorMessage = if (t.message?.contains("Unable to resolve host") == true) {
            "Нет подключения к интернету или сервер недоступен"
        } else {
            "Ошибка сети: ${t.message}"
        }

        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
        loadTestFeelings()
    }
}