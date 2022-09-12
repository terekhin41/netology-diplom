package ru.netology.nerecipe.adapter

import ru.netology.nerecipe.Step

interface StepInteractionListener {
    fun indexedStepDelete(step: Step)
}
