import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline
import pickle

# Load data
df = pd.read_csv('train_data.csv')
X = df['keywords']
y = df['number']

# Create and train pipeline
pipeline = Pipeline([
    ('tfidf', TfidfVectorizer(ngram_range=(1, 2), lowercase=True)),
    ('clf', LogisticRegression(max_iter=1000))
])

pipeline.fit(X, y)

# Save model
with open('model/numerology_model.pkl', 'wb') as f:
    pickle.dump(pipeline, f)

print("Model trained and saved to model/numerology_model.pkl")