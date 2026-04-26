# Adding Begin of validity and end of validity dates for articles.

For articles it is required to add new fields

    - BeginValidityDate: The date when article is valid for operation (mandatory).
    - EndValidityDate: The date when article is no longer valid for operation (optional).

Fix Use Cases involving articles to add these new features. Notice there are no previous articles registered on Database
so backward compatibility issues are not a problem.

When retrieving articles by filter those articles whose end validity date is signed and behind current date
should not be returned.