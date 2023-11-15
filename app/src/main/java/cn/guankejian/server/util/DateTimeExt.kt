package cn.guankejian.server.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

//比较两个日期
infix fun LocalDate.isBefore(that:LocalDate)=  this < that

infix fun LocalDate.isAfter(that:LocalDate)=  this > that

infix fun LocalDateTime.isBefore(that:LocalDateTime)=  this < that

infix fun LocalDateTime.isAfter(that:LocalDateTime)=  this > that

//计算日期间隔
infix fun LocalDate.daysUntil(that: LocalDate): Long = ChronoUnit.DAYS.between(this, that)

infix fun LocalDate.monthsUntil(that: LocalDate): Long = ChronoUnit.MONTHS.between(this, that)

infix fun LocalDate.yearsUntil(that: LocalDate): Long = ChronoUnit.YEARS.between(this, that)

infix fun LocalDateTime.daysUntil(that: LocalDateTime): Long = ChronoUnit.DAYS.between(this, that)

infix fun LocalDateTime.monthsUntil(that: LocalDateTime): Long = ChronoUnit.MONTHS.between(this, that)

infix fun LocalDateTime.yearsUntil(that: LocalDateTime): Long = ChronoUnit.YEARS.between(this, that)

//日期加减运算
infix fun LocalDate.plusYear(years: Long) = this.plusYears(years)

infix fun LocalDate.plusMonth(months: Long) = this.plusMonths(months)

infix fun LocalDate.plusDay(days: Long) = this.plusDays(days)

infix fun LocalDate.minusYear(years: Long) = this.minusYears(years)

infix fun LocalDate.minusMonth(months: Long) = this.minusMonths(months)

infix fun LocalDate.minusDay(days: Long) = this.minusDays(days)

infix fun LocalDateTime.plusYear(years: Long) = this.plusYears(years)

infix fun LocalDateTime.plusMonth(months: Long) = this.plusMonths(months)

infix fun LocalDateTime.plusDay(days: Long) = this.plusDays(days)

infix fun LocalDateTime.minusYear(years: Long) = this.minusYears(years)

infix fun LocalDateTime.minusMonth(months: Long) = this.minusMonths(months)

infix fun LocalDateTime.minusDay(days: Long) = this.minusDays(days)

