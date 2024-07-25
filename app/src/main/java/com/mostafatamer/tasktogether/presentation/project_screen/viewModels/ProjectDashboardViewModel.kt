package com.mostafatamer.tasktogether.presentation.project_screen.viewModels

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mostafatamer.tasktogether.data.repositories.ProjectRepository
import com.mostafatamer.tasktogether.domain.model.Colleague
import com.mostafatamer.tasktogether.domain.model.Group
import com.mostafatamer.tasktogether.domain.model.Project
import com.mostafatamer.tasktogether.domain.model.ProjectStatistics
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class ProjectDashboardViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
) : ProjectViewModel() {


    var projectStatistics by mutableStateOf(ProjectStatistics())
    var colleagues = mutableStateListOf<Colleague>()

    var remainingTime by mutableStateOf("00:00:00:00")
    var remainingTimeMillis by mutableLongStateOf(0L)

    var remainingTimePercentage by mutableDoubleStateOf(0.0)

    override fun init(project: Project, group: Group) {
        super.init(project, group)
        if (project.deadline.time < System.currentTimeMillis()) {
            remainingTimePercentage = 1.0
        }
    }

    private val timer = Timer()

    private val countDownTimer: CountDownTimer by lazy {
        object : CountDownTimer(
            if (project.deadline.time < System.currentTimeMillis()) {
                System.currentTimeMillis() - project.deadline.time
            } else {
                project.deadline.time - System.currentTimeMillis()
            },
            1000
        ) {
            override fun onTick(millisUntilFinished: Long) {

                remainingTimeMillis = millisUntilFinished

                val project = project

                remainingTimePercentage = (
                        (project.deadline.time - project.startDate.time - remainingTimeMillis)
                                / ((project.deadline.time - project.startDate.time).toDouble())
                        )

//                if (project.deadline.time < System.currentTimeMillis()) {
//                    remainingTimePercentage = 0.0
//                }

                formatRemainingTime(millisUntilFinished)
            }

            override fun onFinish() {
                this.cancel()
            }
        }
    }

    override fun onCleared() {
        timer.cancel()
    }

    fun launchRemainingTimeTimer() {
        val startTime =
            abs(project.deadline.time - System.currentTimeMillis())

        var seconds = 0
        timer.schedule(object : TimerTask() {
            override fun run() {
                if (project.deadline.time < System.currentTimeMillis()) {
                    remainingTimeMillis = startTime + seconds
                    seconds += 1000
                    remainingTimePercentage = 1.0
                    remainingTime = "-${formatRemainingTime(remainingTimeMillis)}"
                } else {
                    remainingTimeMillis = startTime - seconds
                    seconds += 1000
                    val project = project
                    remainingTimePercentage = (
                            (project.deadline.time - project.startDate.time - remainingTimeMillis)
                                    / ((project.deadline.time - project.startDate.time).toDouble())
                            )
                    remainingTime = formatRemainingTime(remainingTimeMillis)
                }
            }
        }, 0, 1000)
    }

    private fun formatRemainingTime(millisUntilFinished: Long): String {

        val days = TimeUnit.MILLISECONDS
            .toDays(millisUntilFinished)
        val hours = TimeUnit.MILLISECONDS
            .toHours(millisUntilFinished % TimeUnit.DAYS.toMillis(1))
        val minutes = TimeUnit.MILLISECONDS
            .toMinutes(millisUntilFinished % TimeUnit.HOURS.toMillis(1))
        val seconds = TimeUnit.MILLISECONDS
            .toSeconds(millisUntilFinished % TimeUnit.MINUTES.toMillis(1))

        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds)
    }

    fun getStatistics(onLoad: () -> Unit = {}) {
        projectRepository.getProjectDashBoard(project.id!!)
            .setOnSuccess {
                projectStatistics = it.data
                onLoad()
            }.execute()
    }

    fun getColleagues(onLoad: () -> Unit = {}) {
        projectRepository.getMembersStatistics(project.id!!)
            .setOnSuccess {
                colleagues.clear()
                colleagues.addAll(it.data)
                onLoad()
            }.execute()
    }

}