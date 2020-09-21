package com.example.android.glitchtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.*
import androidx.transition.AutoTransition
import androidx.transition.Transition
import androidx.transition.TransitionManager
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.list_item.view.*
import kotlin.random.Random

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var startSet = ConstraintSet()
    private var endSet = ConstraintSet()
    private var isOnEndSet = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val a = Adapter()
        recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            // Prevents items colors from blinking after refresh
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false

            adapter = a
        }
        a.submitList(listOf("aaaaaa", "bbbbbbbbbbbbbbbbbb", "ccccccccccccccc", "dddddddddddd", "eeeeeeeeeeeeeee"))

        startSet.clone(root.context, R.layout.fragment_first)
        endSet.clone(root.context, R.layout.fragment_first_anim)

        animator.setOnClickListener {
            val transition: Transition = AutoTransition()
            transition.duration = 300
            transition.interpolator = DecelerateInterpolator()
            TransitionManager.beginDelayedTransition(root, transition)

            if (isOnEndSet) {
                startSet.applyTo(root)
            } else {
                endSet.applyTo(root)
            }

            isOnEndSet = !isOnEndSet
        }
    }
}

class Adapter: ListAdapter<String, ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tool = getItem(position)
        holder.bind(tool)
    }
}

class ViewHolder(viewItem: View): RecyclerView.ViewHolder(viewItem) {

    private val listItem = itemView.listItem

    companion object {
        fun from(parent: ViewGroup): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
            return ViewHolder(view)
        }
    }

    fun bind(item: String) {
        listItem.text = item

        val color = (Math.random() * 16777215).toInt() or (0xFF shl 24)
        listItem.setBackgroundColor(color)
    }
}

class DiffCallback: DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}