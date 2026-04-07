from flask import Flask, request, jsonify
from services.predictor import predict_risk
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

@app.route("/")
def home():
    return {"message": "RiskGuard ML Service Running"}

@app.route("/predict", methods=["POST"])
def predict():
    try:
        data = request.json
        result = predict_risk(data)
        return jsonify(result)
    except Exception as e:
        return jsonify({"error": str(e)}), 400

if __name__ == "__main__":
    app.run(debug=True, port=5000)