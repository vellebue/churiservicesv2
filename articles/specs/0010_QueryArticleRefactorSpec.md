# Refactor for service that retrieves articles by filter

The purpose for this spec is refactoring the service **ArticleController.getByFilter** to make it work in paged mode.

## Changes

Rewrite endpoint ArticleController.getByFilter to make it work using the page handler defined for this project. Enable
current fields in filter as fields available for sorting and use article id as tiebreaker field.

