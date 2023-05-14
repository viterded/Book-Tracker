import React, { useState, useEffect } from 'react';
import axios from 'axios';

function App() {
  const [books, setBooks] = useState([]);
  const [newBookTitle, setNewBookTitle] = useState('');

  useEffect(() => {
    async function fetchBooks() {
      const response = await axios.get('/books');
      setBooks(response.data);
    }
    fetchBooks();
  }, []);

  async function addBook() {
    const newBook = {
      title: newBookTitle,
      read: false
    };
    const response = await axios.post('/books', newBook);
    setBooks([...books, response.data]);
    setNewBookTitle('');
  }

  function toggleRead(book) {
    const updatedBook = {
      ...book,
      read: !book.read
    };
    axios.put(`/books/${book._id}`, updatedBook)
      .then(response => {
        const updatedBooks = books.map(b => b._id === response.data._id ? response.data : b);
        setBooks(updatedBooks);
      })
      .catch(error => console.log(error));
  }

  return (
    <div className="App">
      <h1>Book Tracker</h1>
      <ul>
        {books.map(book => (
          <li key={book._id}>
            {book.title} - {book.read ? 'Read' : 'Unread'}
            <button onClick={() => toggleRead(book)}>{book.read ? 'Mark as Unread' : 'Mark as Read'}</button>
          </li>
        ))}
      </ul>
      <input type="text" value={newBookTitle} onChange={e => setNewBookTitle(e.target.value)} />
      <button onClick={addBook}>Add Book</button>
    </div>
  );
}

export default App;
