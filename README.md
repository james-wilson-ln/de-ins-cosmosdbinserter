# CosmosDB Inserter

This is a simple program for adding/updating items in a CosmosDB instance.

## Usage

java com.relx.dataeng.CosmosDBFileUploader _&lt;database key&gt;_ _&lt;database name&gt;_ _&lt;container name&gt;_ _&lt;file path&gt;_

Currently hard-coded to upload to https://localhost:8081

## Running on the emulator

To run on an emulator the JDK needs to have the CosmostDB certificate added. From the Cosmos DB emulator web page
download the certificate (in Chrome use: Context menu &#8594; Inspect &#8594; Security (menu item) &#8594;
View certificate &#8594; Details &#8594; Export...), then run the following from a command line with admin rights:

`keytool -importcert -file <path to certificate you downloaded&gt; -alias cosmosdbemulator -cacerts -storepass <password>`

Notes:

1. Default keystore password is 'changeit' - before adding any external certificates this should, as suggested,
be changed.
1. If you have multiple JDKs installed you might need to do this for other certificate stores - if so then replace the
`-cacerts` with `-keystore '<path to JDK>\lib\security\cacerts'` 
1. The emulator creates a different certificate each time it is started, so this step will need to be repeated.
Multiple certificates cannot be added with the same name so between each run `keytool -delete -alias cosmosdbemulator
-cacerts -storepass <password>` 
