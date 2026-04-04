# Article Units Review Spec

Load table C_ARTICLE_UNITS with the following values:

 Symbol | Description | Key
 UN     | Unit        | article.unit.un= Unit
 BOT    | Bottle      | article.unit.bot= Bottle
 BX     | Box         | article.unit.bx= Box
 PK     | Pack        | article.unit.pk= Pack
 PEU    | Eurpean Pallet      | article.unit.peu= Eurpean Pallet
 PAL    | American Pallet     | article.unit.pal= American Pallet
 
Rewrite messages_es.properties, messages_en.properties, messages_fr.properties, messages_de.properties to include the new article units and remove prior ones.

Fix the tests if required.
