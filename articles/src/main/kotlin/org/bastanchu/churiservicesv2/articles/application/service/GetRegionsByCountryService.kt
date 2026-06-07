package org.bastanchu.churiservicesv2.articles.application.service

import org.bastanchu.churiservicesv2.articles.application.GetRegionsByCountryUseCase
import org.bastanchu.churiservicesv2.articles.application.dto.CountryRegionDto
import org.bastanchu.churiservicesv2.articles.domain.CountryRegionRepository
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class GetRegionsByCountryService(
    private val countryRegionRepository: CountryRegionRepository,
    private val messageSource: MessageSource
) : GetRegionsByCountryUseCase {

    override fun execute(countryId: String): List<CountryRegionDto> {
        return countryRegionRepository.findByCountryId(countryId).map {
            CountryRegionDto(
                countryId = it.countryId,
                regionId = it.regionId,
                description = it.description,
                localizedDescription = messageSource.getMessage(it.regionKey, null, LocaleContextHolder.getLocale())
            )
        }
    }
}
