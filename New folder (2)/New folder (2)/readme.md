# RiskGuard ML API

## 📌 Setup Instructions

1. Install Python (3.8+)

2. Install dependencies:
   pip install -r requirements.txt

3. Run the API:
   python app.py

4. API will run on:
   http://127.0.0.1:5001

---

## 📌 API Endpoint

POST /predict

URL:
http://127.0.0.1:5001/predict

---

## 📌 Request Body (JSON)

{
  "distance": 20,
  "time_stationary": 900,
  "temperature": 38,
  "humidity": 80,
  "weather": 1,
  "hour": 14,
  "day": 2,
  "is_stationary": 1,
  "is_bad_weather": 1,
  "is_night": 0
}

---

## 📌 Response

{
  "risk": 1,
  "confidence": 0.82
}

---

## 📌 Meaning

risk:
0 → Safe  
1 → Stranded / Risk

confidence:
Probability (0 to 1)