# server.py  (run with: uvicorn server:app --reload)
from fastapi import FastAPI
from pydantic import BaseModel
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
import uvicorn

app = FastAPI()
class TextIn(BaseModel):
    text: str

# Load and train once at startup
df = pd.read_csv("train_data.csv")
pipeline = Pipeline([
    ("tfidf", TfidfVectorizer(ngram_range=(1,2))),
    ("clf", LogisticRegression(max_iter=1000))
])
pipeline.fit(df["keywords"], df["number"].astype(str))

@app.post("/predict")
def predict(payload: TextIn):
    pred = int(pipeline.predict([payload.text])[0])
    return {"number": pred}