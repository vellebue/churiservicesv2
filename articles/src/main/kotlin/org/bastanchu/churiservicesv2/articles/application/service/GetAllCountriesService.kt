package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.GetAllCountriesUseCase
import org.bastanchu.churiservicesv2.articles.application.dto.CountryDto
import org.bastanchu.churiservicesv2.articles.domain.CountryRepository
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetAllCountriesService(
    private val countryRepository: CountryRepository,
    private val messageSource: MessageSource
) : GetAllCountriesUseCase {

    override fun execute(): List<CountryDto> {
        return countryRepository.findAll().map {
            CountryDto(
                countryId = it.countryId,
                description = it.description,
                localizedDescription = messageSource.getMessage(it.countryKey, null, LocaleContextHolder.getLocale())
            )
        }
    }
}
