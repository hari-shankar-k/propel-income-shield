import PlanCard from "./PlanCard";
import plans from "../data/plans.json";

export default function Pricing() {
  return (
    <div className="bg-black text-white px-6 lg:px-16 py-16 lg:py-20 border-t border-gray-800">
      {/* Header */}
      <div className="text-center mb-12 lg:mb-16">
        <h2 className="text-3xl lg:text-4xl font-bold mb-4">Simple, Transparent Pricing</h2>
        <p className="text-gray-400 text-base lg:text-lg max-w-2xl mx-auto">
          Choose the perfect plan for your income protection needs. All plans
          include AI-powered risk detection and instant payouts.
        </p>
      </div>

      {/* Plans Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 lg:gap-8 max-w-6xl mx-auto">
        {plans.map((plan) => (
          <PlanCard
            key={plan.id}
            plan={plan}
            isPopular={plan.popular}
          />
        ))}
      </div>

      {/* Footer Note */}
      <div className="text-center mt-12 lg:mt-16">
        <p className="text-gray-400 text-sm lg:text-base">
          🎁 <span className="text-blue-400">Earn Reward Coins</span> on every claim payout
        </p>
      </div>
    </div>
  );
}
