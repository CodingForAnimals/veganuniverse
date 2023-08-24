package org.codingforanimals.veganuniverse.common.utils

sealed class AverageCalculator {
    abstract fun calculate(value: Int, average: Double, count: Long): Double

    object Addition : AverageCalculator() {
        override fun calculate(value: Int, average: Double, count: Long): Double {
            return average + ((value - average) / count)
        }
    }

    object Subtraction : AverageCalculator() {
        override fun calculate(value: Int, average: Double, count: Long): Double {
            return (average * (count + 1) - value) / count
        }
    }
}