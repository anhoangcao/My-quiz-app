package com.example.quizapp

object Constants {

    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        // 1
        val questionOne = Question(
            1,
            "Đây là logo của ngôn ngữ lập trình nào?",
            R.drawable.lg_kotlin,
            arrayListOf("Kotlin", "ReactJS", "NodeJS", "Java"),
            0,
        )
        questionsList.add(questionOne)

        // 2
        val questionTwo = Question(
            2,
            "Đây là logo của ngôn ngữ lập trình nào?",
            R.drawable.reactjs,
            arrayListOf("Kotlin", "ReactJS", "NodeJS", "Java"),
            1,
        )
        questionsList.add(questionTwo)

        // 3
        val questionThree = Question(
            3,
            "Đây là logo của ngôn ngữ lập trình nào?",
            R.drawable.node,
            arrayListOf("Kotlin", "ReactJS", "NodeJS", "Java"),
            2,
        )
        questionsList.add(questionThree)

     return questionsList
    }
}