package com.example.motivationalquotes

object QuoteProvider {
    private val motivationalQuotes = listOf(
        "The only way to do great work is to love what you do. - Steve Jobs",
        "Success is not final, failure is not fatal: it is the courage to continue that counts. - Winston Churchill",
        "Believe you can and you're halfway there. - Theodore Roosevelt",
        "Everything you've ever wanted is on the other side of fear. - George Addair",
        "The future belongs to those who believe in the beauty of their dreams. - Eleanor Roosevelt"
    )

    private val loveQuotes = listOf(
        "The best thing to hold onto in life is each other. - Audrey Hepburn",
        "Love is composed of a single soul inhabiting two bodies. - Aristotle",
        "Where there is love there is life. - Mahatma Gandhi",
        "The greatest happiness of life is the conviction that we are loved. - Victor Hugo",
        "Love is not about possession. Love is about appreciation. - Osho"
    )

    private val successQuotes = listOf(
        "Success usually comes to those who are too busy to be looking for it. - Henry David Thoreau",
        "The road to success and the road to failure are almost exactly the same. - Colin R. Davis",
        "Success is not the key to happiness. Happiness is the key to success. - Albert Schweitzer",
        "Success is walking from failure to failure with no loss of enthusiasm. - Winston Churchill",
        "The secret of success is to do the common thing uncommonly well. - John D. Rockefeller Jr."
    )

    private val happinessQuotes = listOf(
        "Happiness is not something ready-made. It comes from your own actions. - Dalai Lama",
        "The most important thing is to enjoy your life—to be happy. - Audrey Hepburn",
        "Happiness is when what you think, what you say, and what you do are in harmony. - Mahatma Gandhi",
        "The purpose of our lives is to be happy. - Dalai Lama",
        "Happiness is not by chance, but by choice. - Jim Rohn"
    )

    private var lastQuoteIndex: Int = -1

    enum class QuoteCategory {
        MOTIVATIONAL, LOVE, SUCCESS, HAPPINESS
    }

    @Synchronized
    fun getRandomQuote(category: QuoteCategory): String {
        val quotes = when (category) {
            QuoteCategory.MOTIVATIONAL -> motivationalQuotes
            QuoteCategory.LOVE -> loveQuotes
            QuoteCategory.SUCCESS -> successQuotes
            QuoteCategory.HAPPINESS -> happinessQuotes
        }

        if (quotes.isEmpty()) {
            return "Believe in yourself and all that you are."
        }

        var newIndex: Int
        do {
            newIndex = (0 until quotes.size).random()
        } while (newIndex == lastQuoteIndex && quotes.size > 1)

        lastQuoteIndex = newIndex
        return quotes[newIndex]
    }
}
