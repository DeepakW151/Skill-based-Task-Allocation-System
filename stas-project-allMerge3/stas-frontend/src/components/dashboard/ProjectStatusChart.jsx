// import React from "react";
// import { Doughnut } from "react-chartjs-2";
// import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";

// ChartJS.register(ArcElement, Tooltip, Legend);

// const ProjectStatusChart = ({ data }) => {
// 	const chartData = {
// 		// These labels now match the order of the data
// 		labels: ["Pending", "Active", "Completed", "Overdue"],
// 		datasets: [
// 			{
// 				label: "# of Projects",
// 				// This data array now uses the correct keys from your API response
// 				data: [data.pending, data.active, data.completed, data.overdue],
// 				backgroundColor: [
// 					"rgba(245, 158, 11, 0.7)", // Amber for Pending
// 					"rgba(59, 130, 246, 0.7)", // Blue for Active
// 					"rgba(16, 185, 129, 0.7)", // Green for Completed
// 					"rgba(239, 68, 68, 0.7)", // Red for Overdue
// 				],
// 				borderColor: ["#F59E0B", "#3B82F6", "#10B981", "#EF4444"],
// 				borderWidth: 1,
// 			},
// 		],
// 	};

// 	const options = {
// 		responsive: true,
// 		maintainAspectRatio: false,
// 		plugins: {
// 			legend: {
// 				position: "top",
// 			},
// 		},
// 	};

// 	return <Doughnut data={chartData} options={options} />;
// };

// export default ProjectStatusChart;

import React from "react";
import { Doughnut } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";

// Register necessary chart.js components
ChartJS.register(ArcElement, Tooltip, Legend);

const ProjectStatusChart = ({ data, label }) => {
	let labels = [];
	let dataset = [];

	// Determine the chart type based on keys present in data
	if (
		"developers" in data ||
		"managers" in data ||
		"admins" in data ||
		"clients" in data
	) {
		// Admin user role distribution
		labels = ["Developers", "Managers", "Clients", "Admins"];
		dataset = [
			data.developers || 0,
			data.managers || 0,
			data.clients || 0,
			data.admins || 0,
		];
	} else {
		// Client project stats
		labels = ["Active", "Pending", "Completed", "Overdue"];
		dataset = [
			data.active || 0,
			data.pending || 0,
			data.completed || 0,
			data.overdue || 0,
		];
	}

	const chartData = {
		labels,
		datasets: [
			{
				label: label,
				data: dataset,
				backgroundColor: [
					"rgba(59, 130, 246, 0.7)", // Blue
					"rgba(245, 158, 11, 0.7)", // Amber
					"rgba(16, 185, 129, 0.7)", // Green
					"rgba(239, 68, 68, 0.7)", // Red
				],
				borderColor: ["#3B82F6", "#F59E0B", "#10B981", "#EF4444"],
				borderWidth: 1,
			},
		],
	};

	const options = {
		responsive: true,
		maintainAspectRatio: false,
		plugins: {
			legend: { position: "top" },
			title: {
				display: true,
				text: "Dashboard Overview",
			},
		},
	};

	return <Doughnut data={chartData} options={options} />;
};

export default ProjectStatusChart;
