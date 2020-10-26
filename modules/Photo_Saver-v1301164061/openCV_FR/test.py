import requests
import json

def check_new():
    url = 'https://daftrogus.com/check_new.php'
    get_response = requests.get(url)
    json_data = json.loads(get_response.content)
    for i in range(0, len(json_data)):
        uri_per_name = url+'?nama_user='+json_data[i]
        resp = requests.get(uri_per_name)
        json_array = json.loads(resp.content)
        for j in range(0, len(json_array)):
            # /**
            #   * MANDATORY PARAMS
            #   * $_POST['dir'] = Directory of file
            #   * $_POST['nama_user'] = Expected Filename to download
            #   */
            # Download the image
            url = 'https://daftrogus.com/download_file.php'
            mydict = {'dir': './dataset/'+json_data[i]+'/'+json_array[j], 'nama_user': json_array[j]}
            r = requests.post(url, mydict)
            if r.status_code == 200:
                with open("/home/daftrogus/Desktop/boi_"+str(j)+".jpg", 'wb') as f:
                    f.write(r.text)
        # data_str = get_response.content.json().get('nama_anak')
        # data_name = json_data['nama_anak'
