export default function PlanCard({ plan, isPopular }) {
  return (
    <div
      className={`rounded-2xl p-8 transition transform hover:scale-105 ${
        isPopular
          ? "bg-gradient-to-br from-blue-900 to-blue-800 border-2 border-blue-500 shadow-2xl relative"
          : "bg-gray-900 border border-gray-800 hover:border-blue-500"
      }`}
    >
      {/* Popular Badge */}
      {isPopular && (
        <div className="absolute -top-4 left-1/2 transform -translate-x-1/2">
          <span className="bg-blue-600 text-white px-4 py-1 rounded-full text-sm font-semibold">
            Most Popular
          </span>
        </div>
      )}

      {/* Plan Name */}
      <h3 className="text-2xl font-bold mb-2">{plan.name}</h3>

      {/* Price */}
      <div className="mb-6">
        <span className="text-4xl font-bold">₹{plan.price}</span>
        <span className="text-gray-400 text-sm ml-2">/month</span>
      </div>

      {/* CTA Button */}
      <button
        className={`w-full py-3 rounded-xl font-semibold mb-6 transition ${
          isPopular
            ? "bg-white text-blue-900 hover:bg-gray-100"
            : "bg-blue-600 text-white hover:bg-blue-700"
        }`}
      >
        Choose Plan
      </button>

      {/* Features List */}
      <div className="space-y-3">
        <p className="text-gray-400 text-sm font-semibold mb-4">Features:</p>
        {plan.features.map((feature, idx) => (
          <div key={idx} className="flex items-start space-x-3">
            <span className="text-green-400 font-bold">✓</span>
            <span className="text-gray-300 text-sm">{feature}</span>
          </div>
        ))}
      </div>
    </div>
  );
}
