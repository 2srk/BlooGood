<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "blogs";
$authPassword = "yourSecretPassword";

// Only handle POST for adding blogs
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $title = $_POST['title'] ?? null;
    $content = $_POST['content'] ?? null;
    $date = $_POST['date'] ?? null;
    $image1 = $_POST['image1'] ?? null;
    $image2 = $_POST['image2'] ?? null; 
    $receivedPassword = $_POST['password'] ?? null;

    if ($receivedPassword !== $authPassword) {
        die("Unauthorized access");
    }

    $conn = new mysqli($servername, $username, $password, $dbname);

    if ($conn->connect_error) {
        die("Connection failed: " . $conn->connect_error);
    }

    $stmt = $conn->prepare("INSERT INTO blog_data (blog_name, blog_text, date, image1, image2) VALUES (?, ?, ?, ?, ?)");
    $stmt->bind_param("sssss", $title, $content, $date, $image1, $image2);

    if ($stmt->execute()) {
        echo "New blog added successfully";
    } else {
        echo "Error: " . $stmt->error;
    }

    $stmt->close();
    $conn->close();
    exit;
}

// Handle GET request to fetch blog data
$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

$sql = "SELECT blog_id, blog_name, date FROM blog_data";
$result = $conn->query($sql);

$blogs = array();
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        $blogs[] = $row;
    }
}

echo json_encode($blogs);
$conn->close();
?>
