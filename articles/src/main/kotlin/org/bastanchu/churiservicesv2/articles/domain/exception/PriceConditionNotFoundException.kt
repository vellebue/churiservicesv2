package org.bastanchu.churiservicesv2.articles.domain.exception

class PriceConditionNotFoundException(val conditionId: Long) :
    RuntimeException("Price condition with id $conditionId not found")
