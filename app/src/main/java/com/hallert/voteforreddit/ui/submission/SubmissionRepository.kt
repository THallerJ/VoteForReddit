package com.hallert.voteforreddit.ui.submission

import com.hallert.voteforreddit.database.SubmissionDao
import com.hallert.voteforreddit.database.SubmissionEntity
import com.hallert.voteforreddit.util.WebUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import net.dean.jraw.models.Submission
import net.dean.jraw.oauth.AccountHelper
import net.dean.jraw.pagination.DefaultPaginator


class SubmissionRepository(private val submissionDao: SubmissionDao, private val accountHelper: AccountHelper) {
    private lateinit var subreddit: DefaultPaginator<Submission> // TODO: This should be removed
    lateinit var subredditName: String // TODO: This should be removed

    val submissions: Flow<List<Submission>>
        get() = submissionDao.getAllSubmissions().filterNotNull()

    @ExperimentalCoroutinesApi
    val isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    // TODO: Remove buildSubreddit methods
    fun buildSubreddit(subName: String) {
        subreddit =
            accountHelper.reddit.subreddit(subName).posts()
                .build() // This should be passed into the methods as needed
        subredditName = subName
    }

    fun buildSubreddit() {
        subreddit = accountHelper.reddit.frontPage()
            .build() // This should be passed into the methods as needed
        subredditName = "frontpage"
    }

    @ExperimentalCoroutinesApi
    fun getNextPage() {
        isLoading.value = true

        CoroutineScope(IO).launch {
            if (WebUtil.isOnline()) {
                val subs = subreddit.next()
                insertSubmissions(subs)
            }

            isLoading.value = false
        }
    }

    private fun insertSubmissions(submissions: List<Submission>) {
        val submissionList = mutableListOf<SubmissionEntity>()

        for (submission in submissions) {
            val entity = SubmissionEntity(
                submission.id,
                submission,
                System.currentTimeMillis()
            )

            submissionList.add(entity)
        }

        submissionDao.insertSubmissions(submissionList.toList())
    }

    @ExperimentalCoroutinesApi
    fun refresh() {
        isLoading.value = true

        CoroutineScope(IO).launch {
            if (WebUtil.isOnline()) {
                subreddit.restart()
                submissionDao.clearDatabase()
                insertSubmissions(subreddit.next())
                isLoading.value = false
            } else {
                isLoading.value = false
            }
        }
    }
}




