# Serverless Random Notes
> Here we are using the Serverless Framework

- The user that is linked with the serverless cli should have the needed permissions to do all the things we want to do :
  - Manage Lamdbas
  - Manage s3
  - Manage DynamoDb
- The lambda itself should have permissions attached so it can do what it should do
  - DynamoDB permissions :
    ````yaml
    provider:
        ...
        iamRoleStatements:
            - Effect: Allow
            Action:
            - dynamodb:*
            Resource: '*
    ````
- To optimize your lambda functions you can :
  - Use webpack
  - Use specific aws java sdk (not the entire set)