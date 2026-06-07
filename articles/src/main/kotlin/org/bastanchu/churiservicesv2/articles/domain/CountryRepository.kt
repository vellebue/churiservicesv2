package org.bastanchu.churiservicesv2.articles.domain

interface CountryRepository {
    fun findAll(): List<Country>
}
