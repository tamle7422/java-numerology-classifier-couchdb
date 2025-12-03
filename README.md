Numerology Oracle – Your Personal Sacred Number Diviner.  A beautiful, fast, and eternally persistent numerology web app that reveals the hidden number (1–33) behind any text or intention you enter.Live demo: http://localhost:8080 (when running)screenshot (imagine a gorgeous gold-on-black mystical UI here)
Features Stunning dark mystical design with gold accents
Predicts numbers 1–33 (including all Master Numbers) from keywords, affirmations, names, or free-form text
Powered by scikit-learn + rich training data (hundreds of keyword associations)
Lightning-fast predictions via FastAPI (Python)
Elegant Java Spring Boot + Thymeleaf frontend
All readings permanently saved in CouchDB

Quick Start (2 terminals)1. Start the AI brain (FastAPI + scikit-learn)bash

1. Start the AI brain (FastAPI + scikit-learn)bash
cd /path/to/project
pip install fastapi uvicorn scikit-learn pandas
uvicorn server:app --reload

2. Start the frontend
cd backend
mvn spring-boot:run

→ Open http://localhost:8080Type anything:“I am a leader and pioneer”
“heal the world with love”
“money power success”
“spiritual awakening”

…and watch your sacred number appear.

Project Structure

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

Training Data
You can expand train_data.csv forever. Just follow this format:

number,keywords
33,master teacher christ consciousness compassion sacrifice enlightenment healer
1,leader pioneer independent original assertive courageous trailblazer
8,power success authority money wealth executive ambition
...

Persistence (CouchDB)Your readings are permanently saved in a local CouchDB instance.Database: numerology
Credentials: admin:password
View raw data at: http://127.0.0.1:5984/_utils/

All security is pre-configured — no more 401 errors.TroubleshootingModel not responding? → Make sure uvicorn server:app --reload is running
History empty? → Make a prediction first
Want to reset everything?

curl -X DELETE http://admin:password@127.0.0.1:5984/numerology
curl -X PUT http://admin:password@127.0.0.1:5984/numerology

# start fastapi server
uvicorn server:app --workers 1 --loop uvloop --http httptools --no-access-log