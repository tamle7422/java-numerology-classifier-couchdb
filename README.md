# Numerology Oracle – Your Personal Sacred Number Diviner.  A beautiful, fast, and eternally persistent numerology web app that reveals the hidden number (1–33) behind any text or intention you enter.
 - Features Stunning dark mystical design with gold accents.
 - Predicts numbers 1–33 (including all Master Numbers) from keywords, affirmations, names, or free-form text.
 - Powered by scikit-learn + rich training data (hundreds of keyword associations).
 - Predictions via FastAPI (Python).
 - Elegant Java Spring Boot + Thymeleaf front end.
 - All readings permanently saved in CouchDB.

## Quick Start (2 terminals)1. Start the AI brain (FastAPI + scikit-learn)
### 1. Start the AI brain (FastAPI + scikit-learn)bash
```bash
cd /path/to/project
pip install fastapi uvicorn scikit-learn pandas
uvicorn server:app --reload
```

### 2. Start the frontend
```bash
cd backend
mvn spring-boot:run
```

## → Open http://localhost:8080Type anything: “I am a leader and pioneer”, “heal the world with love”, “money power success”, “spiritual awakening”.  And watch your sacred number appear.

## Project Structure
```text
numerology-oracle/
├── train_data.csv              ← All training data (1–33)
├── server.py                   ← FastAPI prediction server
├── model/                      ← (auto-created on first run)
├── backend/
│   ├── src/main/java/...       ← Spring Boot + Thymeleaf
│   ├── src/main/resources/
│   │   ├── templates/index.html
│   │   ├── templates/history.html
│   │   └── application.yml
│   └── pom.xml
└── README.md                   ← this file
```

## Training Data
- You can expand train_data.csv forever. Just follow this format:
- number,keywords
  33,master teacher christ consciousness compassion sacrifice enlightenment healer
  1,leader pioneer independent original assertive courageous trailblazer
  8,power success authority money wealth executive ambition


## Persistence (CouchDB)Your readings are permanently saved in a local CouchDB instance.Database: numerology
 - Credentials: admin:password
 - View raw data at: http://127.0.0.1:5984/_utils/

```bash
curl -X DELETE http://admin:password@127.0.0.1:5984/numerology
curl -X PUT http://admin:password@127.0.0.1:5984/numerology
```

# Start FastAPI Server
```bash
uvicorn server:app --workers 1 --loop uvloop --http httptools --no-access-log
```