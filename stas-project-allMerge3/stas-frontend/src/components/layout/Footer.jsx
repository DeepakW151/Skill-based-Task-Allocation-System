import React from "react";
import { Box, Typography } from "@mui/material";

const Footer = () => (
	<Box
		sx={{
			mt: "auto",
			p: 2,
			backgroundColor: "background.paper",
			borderTop: "1px solid #E5E7EB",
		}}
	>
		<Typography variant="body2" color="text.secondary" align="center">
			© {new Date().getFullYear()} STAS. All Rights Reserved.
		</Typography>
	</Box>
);

export default Footer;
