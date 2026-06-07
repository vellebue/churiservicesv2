# Countries and Regions

This spec is build to provide REST services to retrieve countries list and regions in countries lists.

## Entities

To store info about countries and regions these entities are defined:

Country entity to store on table COUNTRIES with the following fields

- countryId: Primary key 2 chars. It is the country ISO code.
- description: Required 256 chars. It is the colloquian name of the country (In English).
- countryKey: Required 20 chars. It is a key to map the name of the country for I10N message files. It follows
              the pattern: "country.${isoCode}".

Region entity to store on table COUNTRY_REGIONS with the following fields:

- countryId: Primary key 2 chars. It is the country ISO code for this region.
- regionId: Primary key 20 chars. It is the region code for this region according to local region code rules.
- description:Required 256 chars. The name of the region (in English if possible).
- regionKey: Required 50 chars. It is the key to map the name of the region for I10N. It follows the pattern 
              "region.${countryISOCode}.${regionId}".

# Use Cases

Develop the following services.

- A service that retrieves all the country registers.
- A service that retrieves, given a country ISO code, the full set of regions for that given country.

Consider these extra actions:

- Ensure to load the COUNTRIES table with all Contry ISO codes.
- Ensure to load the COUNTRY_REGIONS table with regions initially only for these countries: Spain, France, Germany, UK and USA.
- Populate the corresponding translations for contry keys and region keys into messages files.
- Remember, on response DTOs, not to give the country key or the region key but really its corresponding translation
  depending on the context Locale active.