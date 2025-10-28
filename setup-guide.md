IyBBbmRyb2lkIFZQTiBDbGllbnQgU2V0dXAgR3VpZGUKCiMjIEluc3RhbGxh
dGlvbiBTdGVwcwoKMS4gKipEb3dubG9hZCB0aGUgQVBLKiogIAogICBWaXNp
dCB0aGUgb2ZmaWNpYWwgd2Vic2l0ZSBvciB0cnVzdGVkIHNvdXJjZSB0aG9u
ZSB0aGUgQW5kcm9pZCBWUE4gY2xpZW50IEFQSyBmaWxlLgoKMi4gKipFbmFi
bGUgVW5rbm93biBTb3VyY2VzKiogIAogICAtIEdvIHRvIHlvdXIgZGV2aWNl
J3MgKipTZXR0aW5ncyoqLiAgCiAgIC0gTmF2aWdhdGUgdG8gKipTZWN1cml0
eSogCiAgICAtIEVuYWJsZSArKkVuYWJsZSBVbmtub3duIFNv
dXJjZXM6Kip0byBh
bGxvdw==

## WireGuard Installation

### Step 1: Install WireGuard

To install WireGuard, you can use the package manager for your operating system. For example, on Ubuntu, you can run:

```bash
sudo apt install wireguard
```

### Step 2: Configure WireGuard

After installation, you need to configure WireGuard. Create a configuration file at `/etc/wireguard/wg0.conf`:

```ini
[Interface]
PrivateKey = <your_private_key>
Address = 10.0.0.1/24

[Peer]
PublicKey = <peer_public_key>
Endpoint = <peer_ip>:<peer_port>
AllowedIPs = 10.0.0.2/32
```

### Step 3: Start WireGuard

To start the WireGuard interface, run:

```bash
sudo wg-quick up wg0
```

### Step 4: Enable WireGuard on Boot

To enable WireGuard to start on boot, use:

```bash
sudo systemctl enable wg-quick@wg0
```

### Step 5: Verify the Installation

You can verify that WireGuard is running by checking the status:

```bash
sudo systemctl status wg-quick@wg0
```
