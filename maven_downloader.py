import os
import sys
import requests
import xml.etree.ElementTree as ET


DEFAULT_BASE_URL = "https://repo1.maven.org/maven2"
LIBS_DIR = "libs"


class MavenDownloader:

    def __init__(self, output_dir=LIBS_DIR):
        self.output_dir = output_dir
        os.makedirs(self.output_dir, exist_ok=True)

    def download_dependency(self, group_id, artifact_id, version, repository=None):
        base_url = repository if repository else DEFAULT_BASE_URL
        base_url = base_url.rstrip('/')  # evitar doble //

        group_path = group_id.replace('.', '/')
        jar_name = f"{artifact_id}-{version}.jar"

        url = f"{base_url}/{group_path}/{artifact_id}/{version}/{jar_name}"
        file_path = os.path.join(self.output_dir, jar_name)

        print(f"\n⬇ Descargando {artifact_id}:{version}")
        print(f"Repo: {base_url}")
        print(f"URL: {url}")

        try:
            response = requests.get(url, stream=True)
            if response.status_code != 200:
                print(f"No encontrado: {artifact_id}:{version}")
                return False

            with open(file_path, "wb") as f:
                for chunk in response.iter_content(chunk_size=8192):
                    f.write(chunk)

            print(f"Guardado en: {file_path}")
            return True

        except requests.RequestException as e:
            print(f"Error de red: {e}")
            return False


def parse_dependencies(xml_file):
    """
    Soporta:
    - Formato antiguo (sin repository)
    - Formato nuevo con atributos:
        <dependency repository="..." id="...">
    """

    tree = ET.parse(xml_file)
    root = tree.getroot()

    dependencies = []

    if root.tag == "dependencies":
        deps = root.findall("dependency")
    else:
        deps = [root]

    for dep in deps:
        group_id = dep.findtext("groupId")
        artifact_id = dep.findtext("artifactId")
        version = dep.findtext("version")

        # NUEVO: leer atributo repository
        repository = dep.attrib.get("repository")

        if group_id and artifact_id and version:
            dependencies.append((group_id, artifact_id, version, repository))
        else:
            print(f"⚠ Dependencia inválida ignorada: {ET.tostring(dep, encoding='unicode')}")

    return dependencies


def main(xml_file):
    downloader = MavenDownloader()

    dependencies = parse_dependencies(xml_file)

    if not dependencies:
        print("No se encontraron dependencias válidas")
        return

    print(f"Dependencias encontradas: {len(dependencies)}")

    for group_id, artifact_id, version, repository in dependencies:
        downloader.download_dependency(group_id, artifact_id, version, repository)


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Uso: python download_maven.py dependencies.xml")
        sys.exit(1)

    xml_file = sys.argv[1]
    main(xml_file)