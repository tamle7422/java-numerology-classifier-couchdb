package com.example.numerology.controller;

import com.example.numerology.model.Prediction;
import com.example.numerology.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PredictionController {

    @Autowired
    private PredictionService service;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/predict")
    public String predict(@RequestParam String text, Model model) {
        try {
            int number = service.predict(text);
            service.savePrediction(text, number);
            model.addAttribute("text", text);
            model.addAttribute("number", number);
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("error", "Prediction failed: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/history")
    public String history(Model model) {
        try {
            List<Prediction> history = service.getHistory();
            model.addAttribute("history", history);
        } catch (Exception e) {
            model.addAttribute("error", "Failed to load history");
        }
        return "history";
    }
}