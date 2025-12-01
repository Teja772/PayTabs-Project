# Simple Banking System POC
A Proof of Concept for a microservices-based banking system handling transactions, secure PIN validation, and role-based monitoring.

## 📂 Project Structure
- **system1-gateway**: Spring Boot App (Port 8081) - Routes transactions.
- **system2-corebank**: Spring Boot App (Port 8082) - Manages DB, Balance, and PIN Security.
- **banking-ui**: React App (Port 5173) - Customer and Super Admin Interface.

## 🚀 Setup Instructions

### Prerequisites
- Java JDK 17+
- Node.js & npm

### Step 1: Start System 2 (The Vault)
1. Navigate to `system2-corebank`.
2. Run: `.\mvnw spring-boot:run`
3. *Note: This auto-loads a test user (Card: 4123456789012345 | PIN: 1234 | Bal: 1000).*

### Step 2: Start System 1 (The Gateway)
1. Navigate to `system1-gateway`.
2. Run: `.\mvnw spring-boot:run`

### Step 3: Start the UI
1. Navigate to `banking-ui`.
2. Run: `npm install` (first time only).
3. Run: `npm run dev`.
4. Open http://localhost:5173

---

## 🧪 Test Cases
| Scenario | Inputs | Expected Result |
|----------|--------|-----------------|
| **Success** | Card: `4123456789012345`, PIN: `1234` | SUCCESS (Balance Updates) |
| **Wrong PIN** | Card: `4123456789012345`, PIN: `9999` | FAILURE: Invalid PIN |
| **Bad Range** | Card: `5555...` | DECLINED: Card range not supported |

## 👮 Admin Access
1. Open the UI.
2. Click the **"Super Admin"** button at the top right.
3. Click **"Refresh"** to view the full transaction log from System 2.

## 🔒 Security
- PINs are never stored in plain text.
- System 2 uses SHA-256 Hashing to verify PINs.