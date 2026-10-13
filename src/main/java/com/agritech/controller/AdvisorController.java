package com.agritech.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/advisor")
public class AdvisorController {

    @GetMapping("/irrigation")
    public Map<String, Object> irrigation(
            @RequestParam(defaultValue = "medium") String soil,
            @RequestParam(defaultValue = "normal") String weather,
            @RequestParam(defaultValue = "general") String crop) {

        String s = soil.toLowerCase().trim();
        String w = weather.toLowerCase().trim();
        String c = crop.toLowerCase().trim();

        String frequency;
        String amount;
        String method;
        String note;
        List<String> criticalStages;

        // Crop water requirements and critical stages
        if (c.contains("rice") || c.contains("paddy")) {
            criticalStages = List.of("Tillering", "Panicle Initiation", "Flowering / Heading");
            method = "Controlled Basin Flooding (maintain 2-5 cm shallow standing water)";
            if (w.contains("rain") || w.contains("rainy")) {
                frequency = "Pause irrigation";
                amount = "Drain excess water";
                note = "Paddy needs drainage outlets clear during heavy rains to prevent submergence of young tillers.";
            } else if (w.contains("hot")) {
                frequency = "Daily monitoring";
                amount = "Maintain 4-5 cm water layer";
                note = "High evaporation. Replenish water depth daily and monitor water level gauge.";
            } else {
                frequency = "Every 1-2 days";
                amount = "Maintain 2-3 cm shallow water";
                note = "Adopt alternate wetting and drying (AWD) after vegetative stage to save 25% water and reduce methane.";
            }
        } else if (c.contains("cotton")) {
            criticalStages = List.of("Square formation (35-45 DAS)", "Flowering & Boll development (60-90 DAS)");
            method = "Drip irrigation or alternate furrow";
            if (w.contains("rain") || w.contains("rainy")) {
                frequency = "Pause irrigation";
                amount = "Ensure furrow drainage";
                note = "Cotton cannot tolerate water stagnation for more than 24 hours. Clear furrow drainage.";
            } else if (s.contains("sandy")) {
                frequency = w.contains("hot") ? "Every 2 days" : "Every 3-4 days";
                amount = "25-30 mm per cycle (short cycles)";
                note = "Sandy soils lose water quickly. Drip irrigation at 2 hours/day prevents square shedding.";
            } else if (s.contains("clay")) {
                frequency = w.contains("hot") ? "Every 5-6 days" : "Every 8-10 days";
                amount = "40-50 mm per cycle";
                note = "Black cotton soils hold high moisture. Avoid overwatering to prevent root rot and excessive vegetative growth.";
            } else {
                frequency = w.contains("hot") ? "Every 3-4 days" : "Every 5-6 days";
                amount = "30-35 mm per cycle";
                note = "Maintain steady moisture during boll formation to optimize boll weight and staple quality.";
            }
        } else if (c.contains("maize") || c.contains("corn")) {
            criticalStages = List.of("Tasseling / Knee high stage", "Silking & Cob filling stage");
            method = "Ridge and furrow or sprinkler";
            if (w.contains("rain") || w.contains("rainy")) {
                frequency = "Hold irrigation";
                amount = "Skip cycle";
                note = "Maize is susceptible to temporary waterlogging. Open ends of furrows to let excess rainwater drain.";
            } else if (s.contains("sandy")) {
                frequency = w.contains("hot") ? "Every 2-3 days" : "Every 4 days";
                amount = "25 mm";
                note = "Moisture stress during silking causes poor kernel setting. Maintain root zone moisture.";
            } else {
                frequency = w.contains("hot") ? "Every 4-5 days" : "Every 7-8 days";
                amount = "35-40 mm";
                note = "Critical irrigation at tasseling and cob filling is essential for bold grain formation.";
            }
        } else if (c.contains("chilli") || c.contains("tomato") || c.contains("vegetable")) {
            criticalStages = List.of("Transplanting / Establishment", "Flowering", "Fruit development");
            method = "Drip irrigation with fertigation";
            if (w.contains("rain") || w.contains("rainy")) {
                frequency = "Suspend irrigation";
                amount = "Drain standing water";
                note = "Solanaceous vegetables develop fungal damping-off and fruit rot if roots sit in soggy soil.";
            } else if (s.contains("sandy")) {
                frequency = "Daily or alternate day";
                amount = "15-20 mm (1.5-2 hrs drip)";
                note = "Keep root zone evenly moist. Fluctuating water in tomato/chilli causes blossom end rot and fruit splitting.";
            } else {
                frequency = w.contains("hot") ? "Every 2-3 days" : "Every 4-5 days";
                amount = "25-30 mm";
                note = "Drip irrigation keeps leaves dry, drastically cutting down early blight and powdery mildew.";
            }
        } else if (c.contains("groundnut") || c.contains("peanut")) {
            criticalStages = List.of("Flowering (20-30 DAS)", "Peg penetration (35-50 DAS)", "Pod development");
            method = "Sprinkler or border strip";
            if (w.contains("rain") || w.contains("rainy")) {
                frequency = "Pause irrigation";
                amount = "Skip cycle";
                note = "Avoid waterlogging during pod filling to prevent collar rot and pod rot.";
            } else {
                frequency = s.contains("sandy") ? "Every 4-5 days" : "Every 7-9 days";
                amount = "30 mm";
                note = "Moisture is crucial when pegs enter the soil so pods form easily in loose, friable soil.";
            }
        } else {
            // General / default crops
            criticalStages = List.of("Flowering / Reproductive stage", "Grain / Fruit filling");
            method = "Furrow or Drip system";
            if (w.contains("rain") || w.contains("rainy")) {
                frequency = "Check soil before watering";
                amount = "Skip irrigation if topsoil is moist";
                note = "Avoid waterlogging and clear field drainage channels.";
            } else if (s.contains("sandy")) {
                frequency = w.contains("hot") ? "Every 2 days" : "Every 3-4 days";
                amount = "Short, light cycles (20-25 mm)";
                note = "Sandy soil drains quickly; apply smaller quantities more frequently to prevent leaching.";
            } else if (s.contains("clay")) {
                frequency = w.contains("hot") ? "Every 5-6 days" : "Every 7-10 days";
                amount = "Deep, slow watering (40 mm)";
                note = "Clay holds moisture tightly; allow root zone aeration between watering cycles.";
            } else {
                frequency = w.contains("hot") ? "Every 3-4 days" : "Every 5-6 days";
                amount = "30-35 mm per irrigation";
                note = "Loamy soil offers ideal water retention; water when top 2-3 inches feel dry to touch.";
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("soil", soil);
        result.put("weather", weather);
        result.put("crop", crop);
        result.put("frequency", frequency);
        result.put("amount", amount);
        result.put("method", method);
        result.put("note", note);
        result.put("criticalStages", criticalStages);
        result.put("disclaimer", "Indicative field guidance. Always test root zone soil moisture before turning on pumps.");
        return result;
    }

    @GetMapping("/crop-plan")
    public Map<String, Object> cropPlan(@RequestParam String soil, @RequestParam String season) {
        String s = soil.toLowerCase().trim();
        String se = season.toLowerCase().trim();

        List<String> crops;
        String soilDesc;
        String note;

        if (s.contains("black")) {
            soilDesc = "Black Cotton Soil (Deep, rich in clay, high water retention, neutral to slightly alkaline)";
            if (se.contains("monsoon") || se.contains("kharif")) {
                crops = List.of("Bt Cotton", "Soybean (JS 335 / NRC 37)", "Pigeon Pea (Red Gram)", "Sorghum (Jowar)", "Green Gram");
                note = "Deep black soil retains monsoon moisture exceptionally well. Intercropping Cotton with Red Gram (4:1) or Soybean with Red Gram (4:2) mitigates rainfall risk.";
            } else if (se.contains("winter") || se.contains("rabi")) {
                crops = List.of("Bengal Gram (Chickpea - JG 11)", "Safflower", "Sunflower", "Rabi Sorghum (Maldandi)", "Wheat (HD 2967)");
                note = "Residual moisture in black soil supports chickpea and safflower with 1-2 supplemental irrigations.";
            } else {
                crops = List.of("Green Gram (Summer moong)", "Sesame (Til)", "Fodder Sorghum", "Watermelon (with drip)");
                note = "Black soil requires assured irrigation in summer to prevent deep ground fissures and root desiccation.";
            }
        } else if (s.contains("red")) {
            soilDesc = "Red Soil (Well-drained, sandy-clay loam, rich in iron, slightly acidic to neutral)";
            if (se.contains("monsoon") || se.contains("kharif")) {
                crops = List.of("Groundnut (K-6 / Kadiri)", "Red Gram (Pigeon Pea)", "Maize", "Castor", "Pearl Millet (Bajra)");
                note = "Red soil provides excellent aeration and drainage. Perfect for groundnut pod penetration and taproot crops like castor and pigeon pea.";
            } else if (se.contains("winter") || se.contains("rabi")) {
                crops = List.of("Chickpea", "Horse Gram (Ulavalu)", "Mustard", "Chilli", "Tomato / Onion");
                note = "Red soil has moderate water retention; plan 4-5 supplemental irrigations during the winter season.";
            } else {
                crops = List.of("Irrigated Groundnut", "Sesame (Til)", "Cowpea", "Cluster Bean (Guar)");
                note = "Short-duration summer pulses build soil nitrogen for the upcoming kharif season.";
            }
        } else if (s.contains("sandy")) {
            soilDesc = "Sandy / Light Soil (High drainage, low cation exchange capacity, quick warm-up)";
            if (se.contains("monsoon") || se.contains("kharif")) {
                crops = List.of("Pearl Millet (Bajra)", "Groundnut", "Castor", "Cowpea", "Finger Millet (Ragi)");
                note = "Fast draining. Split nitrogen applications into 3-4 doses to prevent leaching; add FYM or vermicompost to boost water retention.";
            } else if (se.contains("winter") || se.contains("rabi")) {
                crops = List.of("Mustard", "Barley", "Radish & Carrot", "Leafy Vegetables", "Chickpea");
                note = "Light winter temperatures and sandy soils produce clean root vegetables and fast-turnaround brassica crops.";
            } else {
                crops = List.of("Watermelon", "Muskmelon", "Cucumber", "Bottle Gourd", "Cluster Bean");
                note = "Cucurbits flourish in warm sandy soils with drip irrigation or riverbed basin watering.";
            }
        } else {
            // Loamy / Alluvial / Medium
            soilDesc = "Loamy / Alluvial Soil (Optimal silt-clay balance, excellent tilth and fertility)";
            if (se.contains("monsoon") || se.contains("kharif")) {
                crops = List.of("Paddy (Rice)", "Maize", "Cotton", "Sugarcane", "Soybean", "Vegetables");
                note = "Highly fertile soil capable of high crop yields with balanced NPK and micronutrient management.";
            } else if (se.contains("winter") || se.contains("rabi")) {
                crops = List.of("Wheat", "Bengal Gram", "Potato", "Mustard", "Onion", "Tomato", "Green Pea");
                note = "Supports high-value winter cash crops and vegetables with standard irrigation.";
            } else {
                crops = List.of("Summer Moong (Green Gram)", "Summer Rice", "Okra (Bhindi)", "Bitter Gourd", "Fodder Maize");
                note = "Good moisture retention supports 60-day summer green gram or high-demand summer vegetables.";
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("soil", soil);
        result.put("soilDescription", soilDesc);
        result.put("season", season);
        result.put("suggestions", crops);
        result.put("note", note);
        result.put("disclaimer", "Suggestions are indicative. Confirm seed availability, local sowing windows, and soil health card before sowing.");
        return result;
    }

    @GetMapping("/market-guide")
    public List<Map<String, String>> marketGuide() {
        return List.of(
                Map.of("crop", "Paddy (Rice)",
                        "guide", "Maintain moisture at 14-17% for direct APMC mandi purchase without dockage. Check Government MSP (Common ₹2,300/q, Grade-A ₹2,320/q). Clean paddy of chaff and stones to secure top-tier price."),
                Map.of("crop", "Cotton",
                        "guide", "Maintain moisture below 8-12% for maximum grade rate at Cotton Corporation of India (CCI) procurement centers. Check staple length and avoid colored polypropylene twine during picking."),
                Map.of("crop", "Maize (Corn)",
                        "guide", "Keep moisture under 14% to prevent moisture deduction and fungal aflatoxin penalties. Compare local poultry feed factory direct purchase with APMC auction prices."),
                Map.of("crop", "Chilli (Red)",
                        "guide", "Dry to a uniform deep red color with moisture under 10-11%. Grade by variety (Teja, Byadgi, Wonder Hot). If mandi arrivals peak, consider licensed cold storage to sell during post-harvest price recovery."),
                Map.of("crop", "Bengal Gram (Chickpea)",
                        "guide", "Register on e-NAM / NAFED portal for Government MSP procurement. Ensure dockage and foreign matter remain under 2% to qualify for Grade-I procurement."),
                Map.of("crop", "Groundnut",
                        "guide", "Check pod shelling percentage (ideally >70%) and sound mature kernels (SMK). Dry pods thoroughly on clean tarpaulins to avoid moisture staining and aflatoxin risk."),
                Map.of("crop", "Soybean",
                        "guide", "Target seed moisture of 10-12% with foreign matter under 2%. Compare local solvent extraction oil plant gate prices with mandi rates before booking transport.")
        );
    }
}
