package com.hallert.voteforreddit.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hallert.voteforreddit.R
import com.hallert.voteforreddit.ui.comments.CommentFragment
import dagger.hilt.android.AndroidEntryPoint

private lateinit var toolbarTitleTextView: TextView

private const val LAYERED_ACTIVITY_FRAGMENT_INTENT = "layered_activity_intent"
private const val LAYERED_ACTIVITY_SUBMISSION_INTENT = "layered_activity_submission_intent"
private const val COMMENTS_FRAGMENT_TAG: String = "comments_fragment"

@AndroidEntryPoint
class LayeredActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layered)

        toolbarTitleTextView = findViewById(R.id.layered_activity_title)

        // loads the fragment specified by the intent
        when (intent.extras?.getString(LAYERED_ACTIVITY_FRAGMENT_INTENT)) {
            COMMENTS_FRAGMENT_TAG -> {
                val fragment = CommentFragment()

                val bundle = Bundle().apply {
                    putString(
                        "submission_id",
                        intent.extras!!.getString(LAYERED_ACTIVITY_SUBMISSION_INTENT)
                    )
                }

                fragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, COMMENTS_FRAGMENT_TAG).commit()
                toolbarTitleTextView.text = getString(R.string.comments)
            }
        }
    }
}