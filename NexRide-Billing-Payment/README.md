# NexRide Billing & Payment module

This is a standalone Maven project scaffold containing the 7 Billing/Payment files you supplied, plus small supporting model/repository classes so the module can be opened and explored independently in IntelliJ IDEA or VS Code.

## Requirements
- JDK 17 or newer
- IntelliJ IDEA or Visual Studio Code
- Internet access on first Maven import to download dependencies

## Open the project
1. Extract `NexRide-Billing-Payment.zip`.
2. Open the extracted `NexRide-Billing-Payment` folder in IntelliJ IDEA, or use **File → Open Folder** in VS Code.
3. In IntelliJ, trust the project and choose **Load Maven Project** if prompted.
4. Wait for Maven dependencies to finish downloading.

## Run tests
From the project root, run:
```bash
mvn test
```
Or open `src/test/java/com/nexride/NexRidePaymentTest.java` and run the test from the IDE.

## Important notes
- The seven files in `src/main/java/com/nexride/model`, `service`, and `src/test/java/com/nexride` are based on the files you uploaded.
- Minimal supporting classes are included because the uploaded files reference types such as `BaseEntity`, `User`, `StateRepository`, and `ApiException` that were not among the seven supplied files.
- `FileStateRepository` in this standalone scaffold is an in-memory test scaffold, not the complete production persistence layer of your original NexRide application.
- PayHere credentials are intentionally not included. Online checkout stays disabled unless valid configuration is provided. Never commit real merchant secrets or credentials to GitHub.
- Before using this in a real application, integrate the module with your complete NexRide project and review the payment-provider configuration and security.
