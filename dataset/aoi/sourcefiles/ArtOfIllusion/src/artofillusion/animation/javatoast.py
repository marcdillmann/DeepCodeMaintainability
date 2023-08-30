import javalang
import sys
import json

def generate_ast(java_file):
    with open(java_file, 'r') as file:
        java_code = file.read()

    try:
        tree = javalang.parse.parse(java_code)
        return tree
    except javalang.parser.JavaSyntaxError as e:
        print(f"JavaSyntaxError: {e}")
        return None

def to_dict(node):
    if isinstance(node, (javalang.tree.CompilationUnit, list, tuple)):
        return [to_dict(child) for child in node]
    elif isinstance(node, javalang.tree.Node):
        return {
            'type': type(node).__name__,
            'children': [to_dict(child) for child in node.children if isinstance(child, javalang.tree.Node)]
        }
    else:
        return str(node)

def output_ast_to_json(ast, json_file):
    ast_dict = to_dict(ast)
    with open(json_file, 'w') as file:
        json.dump(ast_dict, file, indent=2)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print(f"Usage: {sys.argv[0]} <java_source_file> <json_output_file>")
        sys.exit(1)

    java_file = sys.argv[1]
    json_file = sys.argv[2]
    ast = generate_ast(java_file)
    if ast is not None:
        output_ast_to_json(ast, json_file)

