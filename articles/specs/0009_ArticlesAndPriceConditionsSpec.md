# Articles and Price Conditions

This spec is focused on defining the relationship between Articles and Price Conditions.

## Entities

Create a relationship between Articles and Price Conditions according to the following terms:

- Database table supporting the relationship will be named ARTICLES_PRICE_CONDITIONS(ARTICLE_PK_ID, PRICE_CONDITION_ID)
  with composite PK based on its two columns. It will reference the primary keys of Articles and Price Conditions. Fetch type
  at JPA level will be lazy. Obviously the relationship between Articles and Price Conditions will be N:M.

- From the point of view of the JPA layer there will be a relationship between Articles and Price Conditions but
  it is not required to browse from Price Conditions to Articles so do not map JPA relationship from Price conditions to Articles
  at this level.

- Domain model: **Article** domain entity will have a field priceConditionIds typed List<Long> to keep relationship at
    this level.

## Use Cases

Modify use cases concerning article management operations to add support for linking price conditions with articles as
shown below:

- When creating: **CreateArticleCommand** will add a new field priceConditionIds typed List<Long> to add links to
    previously existing price conditions (they could be empty). On response populate priceConditions on **ArticleDto**
    as described in getting by id later.

- When updating: **UpdateArticleCommand** will also add the new field priceConditionIds typed List<Long> to modify links
    to previously existing price conditions (they could be empty). The same way on response populate priceConditions
    on **ArticleDto** as described in getting by id later.

- When deleting: When deleting an article, the rows in `ARTICLES_PRICE_CONDITIONS` linking it to price conditions must
    be removed, but the referenced `PRICE_CONDITIONS` rows must remain intact.

- When getting by id: There will be an attribute in **ArticleDto** named priceConditions typed List<PriceConditionDto> to
    populate full information about price conditions assigned to this article. Complete PriceConditionRepository to add
    a method to retrieve full data for PriceConditions for a certain article id primary key as a helping procedure.

- When getting by filter: The field named priceConditions in **ArticleDto** will not be populated.
  Keep it as empty list.