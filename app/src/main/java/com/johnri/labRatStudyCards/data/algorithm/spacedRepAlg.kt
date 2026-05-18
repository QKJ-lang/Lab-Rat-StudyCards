package com.johnri.labRatStudyCards.data.algorithm

import com.johnri.labRatStudyCards.data.entity.studyCardEntity
import kotlin.math.max

object spacedRepAlg {
    fun apply(card: studyCardEntity, quality: Int): studyCardEntity {

        var ease = card.easiness
        var reps = card.repetitions
        var interval = card.intervals

        when (quality) {

            0, 1 -> { // Again / Wrong
                reps = 0
                interval = 1
                ease = max(1.3, ease - 0.2)
            }

            2 -> { // Good
                reps += 1
                interval = if (reps == 1) 1
                else if (reps == 2) 3
                else (interval * ease).toInt()
            }

            3 -> { // Easy
                reps += 1
                interval = if (reps == 1) 2
                else (interval * ease * 1.3).toInt()

                ease += 0.1
            }
        }

        return card.copy(
            easiness = ease,
            repetitions = reps,
            intervals = interval,
            nextReview = System.currentTimeMillis() + interval * 86400000L
        )
    }
}