import onnx
model = onnx.load("model/numerology_model.onnx")
for output in model.graph.output:
    print("Output name:", output.name)