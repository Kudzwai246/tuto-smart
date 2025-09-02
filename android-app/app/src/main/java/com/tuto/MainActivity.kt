package com.tuto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TutoApp()
        }
    }
}

@Composable
fun TutoApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Tuto Smart (Demo)", style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.height(12.dp))
            EarningsCalculatorCard()
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Auth & Supabase wiring TODOs are in README and PaymentRepository.kt")
        }
    }
}

@Composable
fun EarningsCalculatorCard() {
    Card(elevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
        var priceUsd by remember { mutableStateOf(50.0) }
        var platformCut by remember { mutableStateOf(0.10) }
        var numberOfOneOnOneStudents by remember { mutableStateOf(0) }
        var averageGroupSize by remember { mutableStateOf(0.0) }
        var numberOfGroupSlots by remember { mutableStateOf(0) }
        var otherIncome by remember { mutableStateOf(0.0) }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Teacher Earnings Calculator", style = MaterialTheme.typography.h6)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(value = priceUsd.toString(), onValueChange = { priceUsd = it.toDoubleOrNull() ?: 0.0 }, label = { Text("price_usd") })
            OutlinedTextField(value = (platformCut*100).roundToInt().toString(), onValueChange = { platformCut = (it.toDoubleOrNull() ?: 10.0)/100.0 }, label = { Text("platform_cut (%)") })
            OutlinedTextField(value = numberOfOneOnOneStudents.toString(), onValueChange = { numberOfOneOnOneStudents = it.toIntOrNull() ?: 0 }, label = { Text("number_of_one_on_one_students") })
            OutlinedTextField(value = averageGroupSize.toString(), onValueChange = { averageGroupSize = it.toDoubleOrNull() ?: 0.0 }, label = { Text("average_group_size") })
            OutlinedTextField(value = numberOfGroupSlots.toString(), onValueChange = { numberOfGroupSlots = it.toIntOrNull() ?: 0 }, label = { Text("number_of_group_slots") })
            OutlinedTextField(value = otherIncome.toString(), onValueChange = { otherIncome = it.toDoubleOrNull() ?: 0.0 }, label = { Text("other_income") })

            Spacer(Modifier.height(10.dp))

            val teacherSharePerStudentPerMonth = teacher_share_per_student_per_month(priceUsd, platformCut)
            val monthlyFromOneOnOne = monthly_income_from_one_on_one(teacherSharePerStudentPerMonth, numberOfOneOnOneStudents)
            val monthlyFromGroups = monthly_income_from_groups(teacherSharePerStudentPerMonth, averageGroupSize, numberOfGroupSlots)
            val monthlyTotal = monthly_income_total(monthlyFromOneOnOne, monthlyFromGroups, otherIncome)
            val gap = gap_to_300(monthlyTotal)

            Text("teacher_share_per_student_per_month = ${\"%.2f\".format(teacherSharePerStudentPerMonth)}")
            Text("monthly_income_from_one_on_one = ${\"%.2f\".format(monthlyFromOneOnOne)}")
            Text("monthly_income_from_groups = ${\"%.2f\".format(monthlyFromGroups)}")
            Text("monthly_income_total = ${\"%.2f\".format(monthlyTotal)}")
            Text("Gap to \$300 = ${\"%.2f\".format(gap)}")

            Spacer(Modifier.height(8.dp))
            Row {
                Button(onClick = { /* TODO create new group */ }) { Text("Create new group") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { /* TODO open trial seats */ }) { Text("Open trial seats") }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { /* TODO promote listing */ }) { Text("Promote listing") }
            }
        }
    }
}

// Exact formula functions

fun teacher_share_per_student_per_month(price_usd: Double, platform_cut: Double): Double {
    return price_usd * (1 - platform_cut)
}

fun monthly_income_from_one_on_one(teacher_share_per_student_per_month: Double, number_of_one_on_one_students: Int): Double {
    return teacher_share_per_student_per_month * number_of_one_on_one_students
}

fun monthly_income_from_groups(teacher_share_per_student_per_month: Double, average_group_size: Double, number_of_group_slots: Int): Double {
    return teacher_share_per_student_per_month * average_group_size * number_of_group_slots
}

fun monthly_income_total(monthly_income_from_one_on_one: Double, monthly_income_from_groups: Double, other_income: Double): Double {
    return monthly_income_from_one_on_one + monthly_income_from_groups + other_income
}

fun gap_to_300(monthly_total: Double): Double {
    return 300.0 - monthly_total
}
