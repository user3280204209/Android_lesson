package cn.edu.sicnu.cardgame

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.edu.sicnu.cardgame.databinding.ItemCardBinding
class CardAdapter(
    private val onCardClick: (Int) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    private var cards: List<Card> = emptyList()

    fun submitCards(newCards: List<Card>) {
        cards = newCards
        @Suppress("NotifyDataSetChanged")
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(binding)
    }

    override fun getItemCount(): Int = cards.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position], position, onCardClick)
    }

    class CardViewHolder(
        private val binding: ItemCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(card: Card, position: Int, onCardClick: (Int) -> Unit) {
            val context = binding.root.context
            binding.root.contentDescription =
                context.getString(R.string.card_description, position + 1)

            when {
                card.isMatched -> {
                    binding.root.text = card.toString()
                    binding.root.setTextColor(
                        ContextCompat.getColor(context, R.color.text_primary)
                    )
                    binding.root.setBackgroundResource(R.drawable.bg_card_matched)
                    binding.root.alpha = 0.75f
                }

                card.isChosen -> {
                    binding.root.text = card.toString()
                    binding.root.setTextColor(
                        ContextCompat.getColor(context, R.color.text_primary)
                    )
                    binding.root.setBackgroundResource(R.drawable.bg_card_chosen)
                    binding.root.alpha = 1f
                }

                else -> {
                    binding.root.text = context.getString(R.string.card_back)
                    binding.root.setTextColor(
                        ContextCompat.getColor(context, R.color.white)
                    )
                    binding.root.setBackgroundResource(R.drawable.bg_card_back)
                    binding.root.alpha = 1f
                }
            }

            binding.root.setOnClickListener {
                onCardClick(position)
            }
        }
    }
}
